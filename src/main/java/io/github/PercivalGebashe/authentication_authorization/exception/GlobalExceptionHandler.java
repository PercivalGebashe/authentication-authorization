package io.github.PercivalGebashe.authentication_authorization.exception;

import io.github.PercivalGebashe.authentication_authorization.dto.ApiResponseDTO;
import jakarta.mail.MessagingException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<ApiResponseDTO> handleBadCredentials(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponseDTO(false, "Invalid email or password", null));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponseDTO> handleDisabledAccount(DisabledException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponseDTO(false, "Please verify your email first", null));
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ApiResponseDTO> handleLockedAccount(LockedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponseDTO(false, "Account is locked", null));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = "User with the given email already exists.";
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponseDTO(false, message, null));
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ApiResponseDTO> handleMessagingException(MessagingException ex){
        System.out.println(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO(
                        false,
                        "Sending email verification failed.",
                        null
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO> handleAll(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO(false, "An error occurred: " + ex.getMessage(), null));
    }
}
