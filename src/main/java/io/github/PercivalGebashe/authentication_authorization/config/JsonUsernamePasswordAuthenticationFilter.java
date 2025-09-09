package io.github.PercivalGebashe.authentication_authorization.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.PercivalGebashe.authentication_authorization.dto.LoginRequestDTO;
import io.github.PercivalGebashe.authentication_authorization.dto.ApiResponseDTO;
import io.github.PercivalGebashe.authentication_authorization.entity.UserLoginDetails;
import io.github.PercivalGebashe.authentication_authorization.exception.AccountNotVerifiedException;
import io.github.PercivalGebashe.authentication_authorization.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Map;

public class JsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtService jwtService;

    public JsonUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super.setAuthenticationManager(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            LoginRequestDTO loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequestDTO.class);

            UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(
                    loginRequest.emailAddress(),
                    loginRequest.password()
                );

            setDetails(request, authRequest);
            Authentication authentication = this.getAuthenticationManager().authenticate(authRequest);

            var userDetails = (UserLoginDetails) authentication.getPrincipal();
            if (!userDetails.isEnabled()) {
                throw new AccountNotVerifiedException();
            }

            return authentication;

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
        ApiResponseDTO errorResponse = new ApiResponseDTO(
            false,
            failed.getMessage() != null ? failed.getMessage() : "Invalid email or password",
            null
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}