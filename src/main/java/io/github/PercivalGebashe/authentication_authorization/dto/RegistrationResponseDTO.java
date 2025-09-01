package io.github.PercivalGebashe.authentication_authorization.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RegistrationResponseDTO(boolean success, Optional<Integer> userId, String message) {
}
