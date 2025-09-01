package io.github.PercivalGebashe.authentication_authorization.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LoginResponseDTO(boolean success, String message, String token) {
}
