package com.mathan.nimbusvault.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyGoogleLinkOtpRequest {

    @NotBlank(message = "Google ID token is required")
    private String idToken;

    @NotBlank(message = "OTP is required")
    private String otp;
}