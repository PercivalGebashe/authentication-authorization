package io.github.PercivalGebashe.authentication_authorization.repository;

import io.github.PercivalGebashe.authentication_authorization.entity.UserLoginDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserLoginDetailsRepository extends JpaRepository<UserLoginDetails, Integer> {
    Optional<UserLoginDetails> findByEmailAddress(String email);
}
