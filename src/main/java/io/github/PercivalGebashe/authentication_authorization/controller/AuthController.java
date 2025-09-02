package io.github.PercivalGebashe.authentication_authorization.controller;

import io.github.PercivalGebashe.authentication_authorization.dto.ApiResponseDTO;
import io.github.PercivalGebashe.authentication_authorization.dto.RegistrationRequestDTO;
import io.github.PercivalGebashe.authentication_authorization.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<ApiResponseDTO> register(@RequestBody RegistrationRequestDTO requestDTO) {
        Integer userId = authService.registerUser(requestDTO);

        if (userId != null) {
            return ResponseEntity.ok(
                new ApiResponseDTO(
                    true,
                    "User registered successfully",
                    Map.of("userId", userId)
                )
            );
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                    new ApiResponseDTO(
                        false,
                        "Registration failed",
                        null
                    )
                );
        }
    }
}