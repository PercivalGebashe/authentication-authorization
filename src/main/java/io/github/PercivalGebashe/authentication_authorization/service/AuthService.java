package io.github.PercivalGebashe.authentication_authorization.service;

import io.github.PercivalGebashe.authentication_authorization.dto.RegistrationRequestDTO;
import io.github.PercivalGebashe.authentication_authorization.entity.EmailValidationStatus;
import io.github.PercivalGebashe.authentication_authorization.entity.UserAccountDetails;
import io.github.PercivalGebashe.authentication_authorization.entity.UserLoginDetails;
import io.github.PercivalGebashe.authentication_authorization.repository.UserAccountDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService{

    private final UserAccountDetailsRepository userAccountDetailsRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserAccountDetailsRepository userAccountDetailsRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userAccountDetailsRepository = userAccountDetailsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Integer registerUser(RegistrationRequestDTO requestDTO){
        UserAccountDetails userAccountDetails = new UserAccountDetails();
        if(requestDTO.firstName().isPresent() && requestDTO.lastName().isPresent()){
            userAccountDetails.setFirstName(requestDTO.firstName().get());
            userAccountDetails.setLastName(requestDTO.lastName().get());
        }

        UserLoginDetails userLoginDetails = new UserLoginDetails();

        userLoginDetails.setEmailAddress(requestDTO.emailAddress());
        userLoginDetails.setPasswordHash(passwordEncoder.encode(requestDTO.password()));
        userLoginDetails.setUserAccountDetails(userAccountDetails);

        userAccountDetails.setUserLoginDetails(userLoginDetails);

        EmailValidationStatus emailValidationStatus = new EmailValidationStatus();
        emailValidationStatus.setEmailValidationStatusId(1);

        userLoginDetails.setEmailValidationStatus(emailValidationStatus);

        UserAccountDetails savedAccountDetails =  userAccountDetailsRepository.save(userAccountDetails);
        return savedAccountDetails.getUserId();
    }
}
