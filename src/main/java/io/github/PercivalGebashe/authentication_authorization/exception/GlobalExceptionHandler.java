package io.github.PercivalGebashe.authentication_authorization.exception;

import io.github.PercivalGebashe.authentication_authorization.dto.LoginResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Optional;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<LoginResponseDTO> handleBadCredentials(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new LoginResponseDTO(false, "Invalid email or password", null));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<LoginResponseDTO> handleDisabledAccount(DisabledException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new LoginResponseDTO(false, "Account is disabled", null));
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<LoginResponseDTO> handleLockedAccount(LockedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new LoginResponseDTO(false, "Account is locked", null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<LoginResponseDTO> handleAll(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new LoginResponseDTO(false, "An error occurred: " + ex.getMessage(), null));
    }
}
