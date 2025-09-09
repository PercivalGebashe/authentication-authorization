package io.github.PercivalGebashe.authentication_authorization.repository;

import io.github.PercivalGebashe.authentication_authorization.entity.UserLoginDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLoginDetailsRepository extends JpaRepository<UserLoginDetails, Integer> {
    Optional<UserLoginDetails> findByEmailAddress(String email);

    Optional<UserLoginDetails> findByConfirmationToken(String confirmationToken);
}
