package io.github.PercivalGebashe.authentication_authorization.repository;

import io.github.PercivalGebashe.authentication_authorization.entity.UserAccountDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountDetailsRepository extends JpaRepository<UserAccountDetails, Integer> {
}
