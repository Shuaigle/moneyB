package com.money.money.controller;


import com.money.money.domain.RegisterResponse;
import com.money.money.domain.RegisterRequest;
import com.money.money.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MoneyController {
    private final AuthenticationService service;

    @GetMapping("/")
    public String helloWorld() {
        return "Hello, World!";
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<RegisterResponse> authenticate(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
