package io.github.PercivalGebashe.authentication_authorization.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserRoleId implements Serializable {

    @Column(name = "UserId")
    private Integer userId;

    @Column(name = "RoleId")
    private Integer roleId;

    public UserRoleId() {}

    public UserRoleId(Integer userId, Integer roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRoleId)) return false;
        UserRoleId that = (UserRoleId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, roleId);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
