package io.github.PercivalGebashe.authentication_authorization.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_roles", schema = "public")
public class UserRole {

    @EmbeddedId
    private UserRoleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "UserId", nullable = false)
    private UserAccountDetails userAccountDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    @JoinColumn(name = "RoleId", nullable = false)
    private Role role;

    public UserRoleId getId() {
        return id;
    }

    public void setId(UserRoleId id) {
        this.id = id;
    }

    public UserAccountDetails getUserAccountDetails() {
        return userAccountDetails;
    }

    public void setUserAccountDetails(UserAccountDetails userAccountDetails) {
        this.userAccountDetails = userAccountDetails;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
