package io.github.PercivalGebashe.authentication_authorization.repository;

import io.github.PercivalGebashe.authentication_authorization.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRolesRepository  extends JpaRepository<UserRole, Integer> {
}
