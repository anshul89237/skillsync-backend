package com.lpu.AuthService.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lpu.AuthService.dto.LoginRequest;
import com.lpu.AuthService.dto.RefreshTokenRequest;
import com.lpu.AuthService.dto.RegisterRequest;
import com.lpu.AuthService.dto.response.AuthResponse;
import com.lpu.AuthService.service.AuthService;
import com.lpu.java.common_security.dto.ApiResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    // 1. SEND OTP
    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<String>> sendOtp(
            @RequestParam @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email) {
        logger.info("Request received to send OTP to email: {}", email);
        return authService.sendOtp(email);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<String>> verifyOtp(
            @RequestParam @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,
            @RequestParam @NotBlank(message = "OTP is required") @Pattern(regexp = "^[0-9]{6}$", message = "OTP must be 6 digits") String otp) {
        logger.info("Request received to verify OTP for email: {}", email);
        return authService.verifyOtp(email, otp);
    }

    // 3. REGISTER (ONLY AFTER OTP VERIFIED)
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest req) {
        logger.info("Registration request received for email: {}", req.getEmail());
        return authService.register(req);
    }

    // 4. LOGIN (JWT)
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest req) {
        logger.info("Login request received for email: {}", req.getEmail());
        return authService.login(req);
    }

    // 🔄 REFRESH TOKEN API (ROTATION)
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {

        logger.info("Refresh token request received");
        return authService.refresh(request);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@Valid @RequestBody com.lpu.AuthService.dto.ForgotPasswordRequest request) {
        logger.info("Forgot password request received for email: {}", request.getEmail());
        return authService.forgotPassword(request);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody com.lpu.AuthService.dto.ResetPasswordRequest request) {
        logger.info("Reset password request received for email: {}", request.getEmail());
        return authService.resetPassword(request);
    }

    @GetMapping("/google-login")
    public void googleLogin(HttpServletResponse response) throws IOException {
        logger.info("Redirecting to Google OAuth2 authorization");
        response.sendRedirect("http://localhost:8080/oauth2/authorization/google");
    }

    @GetMapping("/linkedin-login")
    public void linkedinLogin(HttpServletResponse response) throws IOException {
        logger.info("Redirecting to LinkedIn OAuth2 authorization");
        response.sendRedirect("http://localhost:8080/oauth2/authorization/linkedin");
    }
}