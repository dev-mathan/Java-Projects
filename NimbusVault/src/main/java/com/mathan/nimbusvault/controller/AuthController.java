package com.mathan.nimbusvault.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.http.ResponseEntity;
import com.mathan.nimbusvault.dto.request.VerifyMobileOtpRequest;
import org.springframework.security.core.Authentication;
import com.mathan.nimbusvault.dto.request.RefreshTokenRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mathan.nimbusvault.dto.request.ChangePasswordRequest;

import com.mathan.nimbusvault.dto.request.CheckEmailRequest;
import com.mathan.nimbusvault.dto.request.LoginRequest;
import com.mathan.nimbusvault.dto.request.RegisterRequest;
import com.mathan.nimbusvault.dto.request.ResendEmailVerificationRequest;
import com.mathan.nimbusvault.dto.request.VerifyMobileRequest;
import com.mathan.nimbusvault.dto.response.AuthResponse;
import com.mathan.nimbusvault.service.AuthService;
import com.mathan.nimbusvault.dto.request.VerifyPasswordResetOtpRequest;
import com.mathan.nimbusvault.dto.request.ResetPasswordRequest;

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
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest request,Authentication authentication) {

        String email = authentication.getName();

        authService.logout(request, email);

        return ResponseEntity.ok("Logged out successfully");
    }
    
    // Step 1: Check email
    @PostMapping("/forgot-password/check-email")
    public ResponseEntity<String> checkEmail(@Valid @RequestBody CheckEmailRequest request) {

        return ResponseEntity.ok(
                authService.checkEmail(request));
    }

    // Step 2: Verify mobile and generate OTP
    @PostMapping("/forgot-password/verify-mobile")
    public ResponseEntity<String> verifyMobileAndGenerateOtp(@Valid @RequestBody VerifyMobileRequest request) {

        return ResponseEntity.ok(authService.verifyMobileAndGenerateOtp(request));
    }
    @PostMapping("/verify-mobile-otp")
    public ResponseEntity<String> verifyMobileOtp(@Valid @RequestBody VerifyMobileOtpRequest request) {

        return ResponseEntity.ok(authService.verifyMobileOtp(request));
    }
    @PostMapping("/forgot-password/verify-otp")
    public ResponseEntity<String> verifyPasswordResetOtp(@Valid @RequestBody VerifyPasswordResetOtpRequest request) {

        return ResponseEntity.ok(authService.verifyPasswordResetOtp(request));
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {

        return ResponseEntity.ok(authService.resetPassword(request));
    }
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,Authentication authentication) {

        String email = authentication.getName();

        String response = authService.changePassword(request, email);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/resend-mobile-otp")
    public String resendMobileOtp(@RequestBody Map<String, String> request) {

        String email = request.get("email");

        return authService.resendMobileOtp(email);
    }
    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(
            @RequestParam String token) {

        return ResponseEntity.ok(
                authService.verifyEmail(token));
    }
    @PostMapping("/resend-email-verification")
    public ResponseEntity<String> resendEmailVerification(@Valid @RequestBody ResendEmailVerificationRequest request) {

        return ResponseEntity.ok(
                authService.resendEmailVerification(request));
    }
}