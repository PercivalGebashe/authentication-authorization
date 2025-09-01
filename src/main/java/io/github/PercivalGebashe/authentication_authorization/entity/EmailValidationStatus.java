package io.github.PercivalGebashe.authentication_authorization.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "email_validation_status", schema = "public")
public class EmailValidationStatus {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EmailValidationStatusId")
    private Integer emailValidationStatusId;

    @Column(name = "StatusDescription", nullable = false, unique = true)
    private String statusDescription;

    @OneToMany(mappedBy = "emailValidationStatus")
    private Set<UserLoginDetails> userLoginDetailsSet;

    public Integer getEmailValidationStatusId() {
        return emailValidationStatusId;
    }

    public void setEmailValidationStatusId(Integer emailValidationStatusId) {
        this.emailValidationStatusId = emailValidationStatusId;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public Set<UserLoginDetails> getUserLoginDetailsSet() {
        return userLoginDetailsSet;
    }

    public void setUserLoginDetailsSet(Set<UserLoginDetails> userLoginDetailsSet) {
        this.userLoginDetailsSet = userLoginDetailsSet;
    }
}
