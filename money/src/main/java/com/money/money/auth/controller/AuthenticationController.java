package com.money.money.auth.controller;

import com.money.money.auth.domain.AuthRegisterRequest;
import com.money.money.auth.domain.AuthResponse;
import com.money.money.auth.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @Operation(
            summary = "Login or register by username, email, and password",
            description = "This operation used to login or register a user.")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRegisterRequest request) {
        return ResponseEntity.ok(service.login(request));
    }

    @Operation(
            summary = "Refresh a new access token by validation a refresh token in authorization",
            description = "This operation is to refresh a new access token for authorization.")
    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        service.refreshToken(request, response);
    }

}
