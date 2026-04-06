package com.lpu.AuthService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RefreshTokenRequest {

    @NotBlank(message = "Refresh token is required")
    @Size(min = 20, message = "Invalid refresh token")
    @Pattern(
        regexp = "^[A-Za-z0-9-._]+$",
        message = "Refresh token contains invalid characters"
    )
    private String refreshToken;

    public RefreshTokenRequest() {}

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}