package io.github.PercivalGebashe.authentication_authorization.controller;

import io.github.PercivalGebashe.authentication_authorization.dto.LoginRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserAccountDetailsController {


    @DeleteMapping("/{id}")
    public ResponseEntity<LoginRequestDTO> deleteAccount(@RequestParam(name = "id") Integer id){
        return null;
    }
}
