package io.github.PercivalGebashe.authentication_authorization.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Entity
@Table(name = "user_login_details", schema = "public")
public class UserLoginDetails implements UserDetails {

    @Id
    @Column(name = "UserId")
    private Integer userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "UserId", nullable = false)
    private UserAccountDetails userAccountDetails;

    @Column(name = "EmailAddress", unique = true)
    private String emailAddress;

    @Column(name = "PasswordHash", nullable = false)
    private String passwordHash;

    @Column(name = "PasswordSalt")
    private String passwordSalt;

    @Column(name = "ConfirmationToken")
    private String confirmationToken;

    @Column(name = "TokenGenerationTime")
    private Timestamp tokenExpirationTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EmailValidationStatusId", referencedColumnName = "EmailValidationStatusId", nullable = false)
    private EmailValidationStatus emailValidationStatus;

    @Column(name = "PasswordRecoveryToken")
    private String passwordRecoveryToken;

    @Column(name = "PasswordRecoveryTime")
    private Timestamp passwordRecoveryTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public UserAccountDetails getUserAccountDetails() {
        return userAccountDetails;
    }

    public void setUserAccountDetails(UserAccountDetails userAccountDetails) {
        this.userAccountDetails = userAccountDetails;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public Timestamp getTokenExpirationTime() {
        return tokenExpirationTime;
    }

    public void setTokenExpirationTime(Timestamp tokenExpirationTime) {
        this.tokenExpirationTime = tokenExpirationTime;
    }

    public EmailValidationStatus getEmailValidationStatus() {
        return emailValidationStatus;
    }

    public void setEmailValidationStatus(EmailValidationStatus emailValidationStatus) {
        this.emailValidationStatus = emailValidationStatus;
    }

    public String getPasswordRecoveryToken() {
        return passwordRecoveryToken;
    }

    public void setPasswordRecoveryToken(String passwordRecoveryToken) {
        this.passwordRecoveryToken = passwordRecoveryToken;
    }

    public Timestamp getPasswordRecoveryTime() {
        return passwordRecoveryTime;
    }

    public void setPasswordRecoveryTime(Timestamp passwordRecoveryTime) {
        this.passwordRecoveryTime = passwordRecoveryTime;
    }

    @Override
    public String toString() {
        return "UserLoginDetails{" +
                "userId=" + userId +
                ", userAccountDetails=" + userAccountDetails +
                ", emailAddress='" + emailAddress + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", passwordSalt='" + passwordSalt + '\'' +
                ", confirmationToken='" + confirmationToken + '\'' +
                ", tokenGenerationTime=" + tokenExpirationTime +
                ", emailValidationStatus=" + emailValidationStatus +
                ", passwordRecoveryToken='" + passwordRecoveryToken + '\'' +
                ", passwordRecoveryTime=" + passwordRecoveryTime +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(null == userAccountDetails || null == userAccountDetails.getUserRoles()) {
            return Collections.EMPTY_LIST;
        }

        return userAccountDetails.getUserRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().getRoleDescription()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.passwordHash;
    }

    @Override
    public String getUsername() {
        return this.emailAddress;
    }

    @Override
    public boolean isEnabled() {
        return null != this.emailValidationStatus &&
            null != this.emailValidationStatus.getEmailValidationStatusId() &&
            Integer.valueOf(2).equals(emailValidationStatus.getEmailValidationStatusId());
    }
}
