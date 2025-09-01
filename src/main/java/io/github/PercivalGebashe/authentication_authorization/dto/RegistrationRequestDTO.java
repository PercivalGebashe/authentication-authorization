package io.github.PercivalGebashe.authentication_authorization.dto;

import java.util.Optional;

public record RegistrationRequestDTO(
        Optional<String> firstName,
        Optional<String> lastName,
        String emailAddress,
        String password) {
}
