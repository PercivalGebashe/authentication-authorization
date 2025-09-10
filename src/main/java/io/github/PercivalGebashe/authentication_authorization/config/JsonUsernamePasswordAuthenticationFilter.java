package io.github.PercivalGebashe.authentication_authorization.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.PercivalGebashe.authentication_authorization.dto.LoginRequestDTO;
import io.github.PercivalGebashe.authentication_authorization.dto.ApiResponseDTO;
import io.github.PercivalGebashe.authentication_authorization.entity.UserLoginDetails;
import io.github.PercivalGebashe.authentication_authorization.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtService jwtService;
    private final Validator validator;

    public JsonUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super.setAuthenticationManager(authenticationManager);
        this.jwtService = jwtService;
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            this.validator = validatorFactory.getValidator();
        }
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            LoginRequestDTO loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequestDTO.class);

            Set<ConstraintViolation<LoginRequestDTO>> violations = validator.validate(loginRequest);
            if (!violations.isEmpty()) {
                Map<String, Object> errors = violations.stream()
                        .collect(Collectors.groupingBy(
                                v -> v.getPropertyPath().toString(),
                                Collectors.mapping(ConstraintViolation::getMessage, Collectors.toList())
                        ))
                        .entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue
                        ));

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                ApiResponseDTO errorResponse = new ApiResponseDTO(
                        false,
                        "Input Validation failed",
                        errors);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                objectMapper.writeValue(response.getWriter(), errorResponse);
                return null;
            }

            UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(
                    loginRequest.emailAddress(),
                    loginRequest.password()
                );

            setDetails(request, authRequest);

            return this.getAuthenticationManager().authenticate(authRequest);

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse login request", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {
        var userDetails = (UserLoginDetails) authResult.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        ApiResponseDTO loginResponse = new ApiResponseDTO(
            true,
            "Login successful",
            Map.of("token", token)
        );

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), loginResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        String message = "Invalid email or password";

        if (failed instanceof DisabledException) {
            message = "Please verify your email first";
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        ApiResponseDTO errorResponse = new ApiResponseDTO(false, message, null);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }

}