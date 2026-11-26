
package com.mathan.nimbusvault.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mathan.nimbusvault.dto.request.GoogleLoginRequest;
import com.mathan.nimbusvault.dto.request.VerifyGoogleLinkOtpRequest;
import com.mathan.nimbusvault.dto.request.GoogleLinkOtpRequest;
import com.mathan.nimbusvault.dto.response.AuthResponse;
import com.mathan.nimbusvault.service.GoogleAuthService;
import com.mathan.nimbusvault.service.GoogleLinkOtpService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class GoogleAuthController {

    private final GoogleAuthService googleAuthService;
    private final GoogleLinkOtpService googleLinkOtpService;

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleLogin(
            @Valid @RequestBody GoogleLoginRequest request)
            throws GeneralSecurityException, IOException {

        AuthResponse response =
                googleAuthService.googleLogin(request.getIdToken());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/google/link/request-otp")
    public ResponseEntity<String> requestGoogleLinkOtp(
            @Valid @RequestBody GoogleLinkOtpRequest request)
            throws GeneralSecurityException, IOException {

        String response = googleLinkOtpService.sendGoogleLinkOtp(
                request.getIdToken());

        return ResponseEntity.ok(response);
    }

@PostMapping("/google/link/verify-otp")
public ResponseEntity<String> verifyGoogleLinkOtp(
        @Valid @RequestBody VerifyGoogleLinkOtpRequest request)
        throws GeneralSecurityException, IOException {

    String response = googleLinkOtpService.verifyGoogleLinkOtp(
            request.getIdToken(),
            request.getOtp());

    return ResponseEntity.ok(response);
}

}