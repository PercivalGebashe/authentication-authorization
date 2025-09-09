package io.github.PercivalGebashe.authentication_authorization.util;

import io.github.PercivalGebashe.authentication_authorization.dto.ApiResponseDTO;

import java.util.Map;

public class ApiResponseBuilder {

    public static ApiResponseDTO success(String message, Map<String, Object> data) {
        return new ApiResponseDTO(true, message, data);
    }

    public static ApiResponseDTO failure(String message) {
        return new ApiResponseDTO(false, message, null);
    }

    public static ApiResponseDTO failure(String message, Map<String, Object> data) {
        return new ApiResponseDTO(false, message, data);
    }
}
