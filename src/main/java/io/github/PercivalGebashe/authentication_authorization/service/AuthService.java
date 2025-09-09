package io.github.PercivalGebashe.authentication_authorization.service;

import io.github.PercivalGebashe.authentication_authorization.dto.RegistrationRequestDTO;
import io.github.PercivalGebashe.authentication_authorization.entity.EmailValidationStatus;
import io.github.PercivalGebashe.authentication_authorization.entity.UserAccountDetails;
import io.github.PercivalGebashe.authentication_authorization.entity.UserLoginDetails;
import io.github.PercivalGebashe.authentication_authorization.repository.EmailValidationStatusRepository;
import io.github.PercivalGebashe.authentication_authorization.repository.UserAccountDetailsRepository;
import io.github.PercivalGebashe.authentication_authorization.repository.UserLoginDetailsRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
public class AuthService{

    private final UserAccountDetailsRepository userAccountDetailsRepository;
    private final UserLoginDetailsRepository userLoginDetailsRepository;
    private final EmailValidationStatusRepository emailValidationStatusRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public AuthService(UserAccountDetailsRepository userAccountDetailsRepository, UserLoginDetailsRepository userLoginDetailsRepository, EmailValidationStatusRepository emailValidationStatusRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userAccountDetailsRepository = userAccountDetailsRepository;
        this.userLoginDetailsRepository = userLoginDetailsRepository;
        this.emailValidationStatusRepository = emailValidationStatusRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }



    public Integer registerUser(RegistrationRequestDTO requestDTO, String baseUrl) throws MessagingException {
        UserAccountDetails userAccountDetails = new UserAccountDetails();
        if(requestDTO.firstName().isPresent() && requestDTO.lastName().isPresent()){
            userAccountDetails.setFirstName(requestDTO.firstName().get());
            userAccountDetails.setLastName(requestDTO.lastName().get());
        }

        UserLoginDetails userLoginDetails = new UserLoginDetails();

        userLoginDetails.setEmailAddress(requestDTO.emailAddress());
        userLoginDetails.setPasswordHash(passwordEncoder.encode(requestDTO.password()));
        userLoginDetails.setUserAccountDetails(userAccountDetails);
        userLoginDetails.setConfirmationToken(generateVerificationToken());
        userLoginDetails.setTokenExpirationTime(Timestamp.valueOf(LocalDateTime.now().plusHours(24)));

        userAccountDetails.setUserLoginDetails(userLoginDetails);

        EmailValidationStatus emailValidationStatus = new EmailValidationStatus();
        emailValidationStatus.setEmailValidationStatusId(1);
        emailValidationStatus.setStatusDescription("PENDING");

        userLoginDetails.setEmailValidationStatus(emailValidationStatus);

        UserAccountDetails savedAccountDetails =  userAccountDetailsRepository.save(userAccountDetails);
        sendVerificationEmail(savedAccountDetails.getUserLoginDetails(), baseUrl);

        return savedAccountDetails.getUserId();
    }



    public void verifyUser(String verificationToken){
        Optional<UserLoginDetails> userLoginOptional = userLoginDetailsRepository.findByConfirmationToken(verificationToken);

        if(userLoginOptional.isPresent()){
            UserLoginDetails userLoginDetails = userLoginOptional.get();

            if(userLoginDetails.getTokenExpirationTime().before(Timestamp.valueOf(LocalDateTime.now()))){
                throw new RuntimeException("Verification Token expired");
            }

            EmailValidationStatus verifiedStatus = emailValidationStatusRepository.findById(2)
                    .orElseThrow(() -> new RuntimeException("Email validation status not found"));
            verifiedStatus.setStatusDescription("VERIFIED");

            userLoginDetails.setEmailValidationStatus(verifiedStatus);
            userLoginDetails.setConfirmationToken(null);
            userLoginDetails.setTokenExpirationTime(null);
            userLoginDetailsRepository.save(userLoginDetails);

        }else {
            throw new RuntimeException("Invalid verification token");
        }
    }

    public void sendVerificationToken(String emailAddress, String baseUrl) throws MessagingException {
        Optional<UserLoginDetails> userLoginOptional = userLoginDetailsRepository.findByEmailAddress(emailAddress);

        if(userLoginOptional.isPresent()){
            UserLoginDetails userLoginDetails = userLoginOptional.get();
            if(userLoginDetails.isEnabled()){
                throw new RuntimeException("Account is already verified");
            }
            userLoginDetails.setConfirmationToken(generateVerificationToken());
            userLoginDetails.setTokenExpirationTime(Timestamp.valueOf(LocalDateTime.now().plusHours(24)));
            sendVerificationEmail(userLoginDetails, baseUrl);
            userLoginDetailsRepository.save(userLoginDetails);
        }else{
            throw new RuntimeException("User not found");
        }
    }

    public void sendVerificationEmail(UserLoginDetails loginDetails, String baseUrl) throws MessagingException {

        String subject = "Account Verification";
        String htmlMessage = String.format("""
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    </head>
    <body style="font-family: Arial, sans-serif; background-color: #f9f9f9; margin:0; padding:20px;">
        <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 25px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
            <h2 style="color: #222; font-size: 22px; margin-bottom: 15px;">Verify Your Account</h2>
            <p style="font-size: 16px; color: #333; line-height: 1.5;">
                Thank you for registering! Please click the button below to verify your email address and activate your account.
            </p>
            
            <!-- Verification Button -->
            <div style="text-align: center; margin: 25px 0;">
                <a href="%s/api/v1/auth/verify?token=%s"
                   style="display: inline-block; padding: 15px 30px; font-size: 16px; background-color: #666666; color: #ffffff; text-decoration: none; border-radius: 6px; font-weight: bold; width: 80%%; max-width: 300px;">
                   Verify Account
                </a>
            </div>
            
            <!-- Copy link fallback -->
            <p style="font-size: 14px; color: #555; line-height: 1.4;">
                If the button doesn't work, copy and paste the following link into your browser:
            </p>
            <p style="word-break: break-all; font-size: 14px; color: #0073aa; line-height: 1.4;">
                %s/api/v1/auth/verify?token=%s
            </p>
            
            <p style="font-size: 12px; color: #888; margin-top: 25px;">
                If you did not request this, please ignore this email.
            </p>
        </div>
    </body>
</html>
""", baseUrl, loginDetails.getConfirmationToken(), baseUrl, loginDetails.getConfirmationToken());


        emailService.sendVerification(loginDetails.getUsername(), subject, htmlMessage);
    }

    public static String generateVerificationToken() {
        byte[] randomBytes = new byte[24];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);
        Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();
        return base64Encoder.encodeToString(randomBytes);
    }
}
