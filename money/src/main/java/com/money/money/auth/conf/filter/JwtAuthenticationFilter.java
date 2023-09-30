package com.money.money.auth.conf.filter;

import com.money.money.auth.service.JwtService;
import com.money.money.repository.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    private static final String AUTH_PATH = "/auth";
    private static final String AUTH_HEADER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain) throws ServletException, IOException {

        if (hasAuthPathInRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        var jwt = extractJwtFromRequest(request);
        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        var userName = jwtService.extractUsername(jwt);
        if (userName == null || isUserAlreadyAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

        var userDetails = userDetailsService.loadUserByUsername(userName);
        if (isJwtAndUserValid(jwt, userDetails)) {
            setAuthentication(request, userDetails);
        }

        filterChain.doFilter(request, response);
    }

    private boolean hasAuthPathInRequest(HttpServletRequest request) {
        return request.getServletPath().contains(AUTH_PATH);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith(AUTH_HEADER_PREFIX)) {
            return authHeader.substring(AUTH_HEADER_PREFIX.length());
        }
        return null;
    }

    private boolean isUserAlreadyAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }

    private boolean isJwtAndUserValid(String jwt, UserDetails userDetails) {
        return jwtService.isTokenValid(jwt, userDetails) && isTokenInRepositoryValid(jwt);
    }

    private boolean isTokenInRepositoryValid(String jwt) {
        return tokenRepository.findByToken(jwt)
            .map(t -> !t.isExpired() && !t.isRevoked())
            .orElse(false);
    }

    private void setAuthentication(HttpServletRequest request, UserDetails userDetails) {
        var authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );
        authToken.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
