package com.mathan.nimbusvault.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleLinkOtpRequest {

    @NotBlank(message = "Google ID token is required")
    private String idToken;
}