package com.money.money.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.money.money.auth.domain.AuthResponse;
import com.money.money.auth.domain.AuthRegisterRequest;
import com.money.money.domain.MoneyUser;
import com.money.money.domain.Token;
import com.money.money.domain.TokenType;
import com.money.money.global.exception.MissingAuthHeaderException;
import com.money.money.repository.MoneyUserRepository;
import com.money.money.repository.TokenRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final MoneyUserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  @Transactional
  public AuthResponse register(AuthRegisterRequest request) {
    var user = MoneyUser.builder()
        .username(request.getUsername())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .build();
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    return AuthResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  @Transactional
  public AuthResponse login(AuthRegisterRequest request) {
    Authentication authentication = authenticateUserLogin(request);
    if(authentication.isAuthenticated()) {
      var user = repository.findByUsername(request.getUsername())
              .orElseThrow(() -> new EntityNotFoundException("User not found!"));
      var jwtToken = jwtService.generateToken(user);
      var refreshToken = jwtService.generateRefreshToken(user);
      revokeAllUserTokens(user);
      saveUserToken(user, jwtToken);
      return AuthResponse.builder()
              .accessToken(jwtToken)
              .refreshToken(refreshToken)
              .build();
    } else {
      return this.register(request);
    }
  }

  private Authentication authenticateUserLogin(AuthRegisterRequest request) {
    return authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
            )
    );
  }

  private void saveUserToken(MoneyUser user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(MoneyUser user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userName;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new MissingAuthHeaderException("No Authorization header found or it does not start with 'Bearer '");
    }

    refreshToken = authHeader.substring(7);
    userName = jwtService.extractUsername(refreshToken);
    if (userName != null) {
      var user = this.repository.findByUsername(userName)
              .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userName));
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
