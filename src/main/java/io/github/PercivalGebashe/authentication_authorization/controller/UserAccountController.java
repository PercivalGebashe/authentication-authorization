package io.github.PercivalGebashe.authentication_authorization.controller;

import io.github.PercivalGebashe.authentication_authorization.dto.ApiResponseDTO;
import io.github.PercivalGebashe.authentication_authorization.service.UserAccountService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/user")
public class UserAccountController {

    private final UserAccountService userAccountService;

    @Autowired
    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @DeleteMapping("/request-delete")
    public ResponseEntity<ApiResponseDTO> requestDeleteAccount(@RequestParam(name = "email") String email) throws MessagingException {
        System.out.println("vsdoivhhi");
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        userAccountService.requestDeleteAccount(email, baseUrl);

        return ResponseEntity.ok(new ApiResponseDTO(
                true,
                "Account delete request received. Delete confirmation email sent.",
                null
        ));
    }

    @GetMapping("/confirm-delete")
    public ResponseEntity<ApiResponseDTO> confirmDelete(@RequestParam String token) {
        userAccountService.confirmDelete(token);

        return ResponseEntity.ok(new ApiResponseDTO(
                true,
                "User account successfully deleted",
                null
        ));
    }
}