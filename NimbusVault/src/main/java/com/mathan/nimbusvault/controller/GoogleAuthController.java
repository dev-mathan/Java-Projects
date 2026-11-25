
package com.mathan.nimbusvault.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mathan.nimbusvault.dto.request.GoogleLoginRequest;
import com.mathan.nimbusvault.dto.response.AuthResponse;
import com.mathan.nimbusvault.service.GoogleAuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class GoogleAuthController {

    private final GoogleAuthService googleAuthService;

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleLogin(
            @Valid @RequestBody GoogleLoginRequest request)
            throws GeneralSecurityException, IOException {

        AuthResponse response =
                googleAuthService.googleLogin(request.getIdToken());

        return ResponseEntity.ok(response);
    }
}
