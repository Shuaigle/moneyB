package com.money.money.auth.service;

import com.money.money.auth.domain.AuthRegisterRequest;
import com.money.money.domain.MoneyUser;
import com.money.money.repository.MoneyUserRepository;
import com.money.money.repository.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthenticationServiceTest {
    @Mock
    private MoneyUserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private AuthenticationService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser() {
        AuthRegisterRequest request = new AuthRegisterRequest("username", "password", "email");
        MoneyUser user = MoneyUser.builder()
                .username("username")
                .password("password")
                .email("email")
                .build();

        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any())).thenReturn("token");
        when(repository.save(any())).thenReturn(user);

        var result = service.register(request);

        assertEquals("token", result.getAccessToken());
    }

    @Test
    void loginUser() {
        AuthRegisterRequest request = new AuthRegisterRequest("username", "password", "email");
        MoneyUser user = MoneyUser.builder()
                .username("username")
                .password("password")
                .email("email")
                .build();
        Authentication auth = new UsernamePasswordAuthenticationToken("username", "password");

        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(repository.findByUsername(any())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any())).thenReturn("token");

        var result = service.login(request);

        assertEquals("token", result.getAccessToken());
    }
}
