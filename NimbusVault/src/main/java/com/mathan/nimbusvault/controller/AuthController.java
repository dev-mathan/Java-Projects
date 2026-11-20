package com.mathan.nimbusvault.controller;

import org.springframework.http.ResponseEntity;
import com.mathan.nimbusvault.dto.request.RefreshTokenRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mathan.nimbusvault.dto.request.LoginRequest;
import com.mathan.nimbusvault.dto.request.RegisterRequest;
import com.mathan.nimbusvault.dto.response.AuthResponse;
import com.mathan.nimbusvault.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {

        authService.register(request);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {

        AuthResponse response = authService.refreshAccessToken(request);

        return ResponseEntity.ok(response);
    }
}