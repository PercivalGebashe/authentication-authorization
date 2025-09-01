package io.github.PercivalGebashe.authentication_authorization.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "user_account_details", schema = "public")
public class UserAccountDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId")
    private Integer userId;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "LastName")
    private String lastName;

    @OneToOne(mappedBy = "userAccountDetails", cascade = CascadeType.ALL)
    private UserLoginDetails userLoginDetails;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserLoginDetails getUserLoginDetails() {
        return userLoginDetails;
    }

    public void setUserLoginDetails(UserLoginDetails userLoginDetails) {
        this.userLoginDetails = userLoginDetails;
    }
}
