package com.money.money.auth.controller;

import com.money.money.auth.domain.AuthRegisterRequest;
import com.money.money.auth.domain.AuthResponse;
import com.money.money.auth.domain.SSOTokenResponse;
import com.money.money.auth.service.AuthenticationService;
import com.money.money.global.utils.RedisUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.money.money.global.utils.RedisUtil.SSO_KEY_SET;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final RedisUtil redisUtil;

    @Operation(
        summary = "Login or register by username, email, and password",
        description = "This operation used to log in or register a user."
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRegisterRequest request) {
        return ResponseEntity.ok(service.login(request));
    }

    @Operation(
        summary = "SSO login",
        description = "This operation used to sso log in a user."
    )
    @PostMapping("/sso/login")
    public ResponseEntity<AuthResponse> ssoLogin(@RequestBody AuthRegisterRequest request) {
        if (StringUtils.isBlank(request.getToken())) {
            return ResponseEntity.badRequest().build();
        } else if (!redisUtil.getSetMembers(SSO_KEY_SET).contains(request.getToken())) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(service.login(request));
    }

    @Operation(
        summary = "Refresh a new access token by validation a refresh token in authorization",
        description = "This operation is to refresh a new access token for authorization."
    )
    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        service.refreshToken(request, response);
    }

    @Operation(
        summary = "Get SSO token before a SSO login",
        description = "This operation is to provide an SSO token for SSO login."
    )
    @GetMapping("/sso/token")
    public ResponseEntity<SSOTokenResponse> ssoToken() {
        var response = new SSOTokenResponse(UUID.randomUUID());
        var status = redisUtil.addToSetWithExpiration(
            SSO_KEY_SET,
            response,
            RedisUtil.TIME_IN_SECONDS,
            TimeUnit.SECONDS
        );
        if (status == null) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(response);
    }

}
