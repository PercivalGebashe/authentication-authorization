package io.github.PercivalGebashe.authentication_authorization.controller;

import io.github.PercivalGebashe.authentication_authorization.dto.ApiResponseDTO;
import io.github.PercivalGebashe.authentication_authorization.dto.RegistrationRequestDTO;
import io.github.PercivalGebashe.authentication_authorization.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<ApiResponseDTO> register(@Valid @RequestBody RegistrationRequestDTO requestDTO) throws MessagingException {
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        System.out.println("Base URL:" + baseUrl);
        Integer userId = authService.registerUser(requestDTO, baseUrl);

        if (userId != null) {
            return ResponseEntity.ok(
                new ApiResponseDTO(
                    true,
                    "User registered successfully.\nCheck email for account verification link.",
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

    @GetMapping(value = "/verify")
    public ResponseEntity<ApiResponseDTO> verifyAccount(@RequestParam(name = "token") String verificationToken){
        authService.verifyUser(verificationToken);

        return ResponseEntity.ok(new ApiResponseDTO(true,
                "Account verification successful",
                null));
    }

    @GetMapping(value = "/resend-verification")
    public ResponseEntity<ApiResponseDTO> resendVerification(@RequestParam(name = "email") String email)
    throws MessagingException {
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        System.out.println("Base URL:" + baseUrl);
        authService.sendVerificationToken(email, baseUrl);
        return ResponseEntity.ok(new ApiResponseDTO(
                true,"Email verification link sent",
                null
                )
        );
    }
}