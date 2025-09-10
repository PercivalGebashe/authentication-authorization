package io.github.PercivalGebashe.authentication_authorization.service;

import io.github.PercivalGebashe.authentication_authorization.entity.UserAccountDetails;
import io.github.PercivalGebashe.authentication_authorization.entity.UserLoginDetails;
import io.github.PercivalGebashe.authentication_authorization.repository.UserAccountDetailsRepository;
import io.github.PercivalGebashe.authentication_authorization.repository.UserLoginDetailsRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class UserAccountService {

    private final UserAccountDetailsRepository userAccountDetailsRepository;
    private final UserLoginDetailsRepository userLoginDetailsRepository;
    private final EmailService emailService;

    @Autowired
    public UserAccountService(UserAccountDetailsRepository userAccountDetailsRepository,
                              UserLoginDetailsRepository userLoginDetailsRepository,
                              EmailService emailService) {
        this.userAccountDetailsRepository = userAccountDetailsRepository;
        this.userLoginDetailsRepository = userLoginDetailsRepository;
        this.emailService = emailService;
    }

    public void requestDeleteAccount(String emailAddress, String baseUrl) throws MessagingException {
        UserLoginDetails loginDetails = userLoginDetailsRepository.findByEmailAddress(emailAddress)
                .orElseThrow(() -> new RuntimeException("User does not exist"));


        loginDetails.setConfirmationToken(AuthService.generateVerificationToken());
        loginDetails.setTokenExpirationTime(Timestamp.valueOf(LocalDateTime.now().plusHours(24)));

        userLoginDetailsRepository.save(loginDetails);

        sendConfirmationEmail(loginDetails, baseUrl);
    }

    @Transactional
    public void confirmDelete(String verificationToken) {
        UserLoginDetails loginDetails = userLoginDetailsRepository
                .findByConfirmationToken(verificationToken)
                .orElseThrow(() -> new RuntimeException("Invalid confirmation token"));

        if (loginDetails.getTokenExpirationTime().before(Timestamp.valueOf(LocalDateTime.now()))) {
            throw new RuntimeException("Confirmation token expired");
        }

        // Delete the parent; cascade ensures child is deleted
        UserAccountDetails accountDetails = loginDetails.getUserAccountDetails();
        userAccountDetailsRepository.delete(accountDetails);
    }

    public void sendConfirmationEmail(UserLoginDetails loginDetails, String baseUrl) throws MessagingException {
        String token = loginDetails.getConfirmationToken();
        String subject = "Confirm Account Deletion";

        String htmlMessage = String.format("""
        <html>
            <body style="font-family: Arial, sans-serif;">
                <h2>Confirm Account Deletion</h2>
                <p>Click the button below to confirm your account deletion:</p>
                <a href="%s/api/v1/user/confirm-delete?token=%s"
                   style="padding: 12px 25px; background-color: #666; color: #fff; text-decoration: none; border-radius: 6px;">
                   Delete Account
                </a>
                <p>If the button doesn't work, copy and paste the link below:</p>
                <p>%s/api/v1/user/confirm-delete?token=%s</p>
            </body>
        </html>
        """, baseUrl, token, baseUrl, token);

        emailService.sendVerification(loginDetails.getEmailAddress(), subject, htmlMessage);
    }
}
