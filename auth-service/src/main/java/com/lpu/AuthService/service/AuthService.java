package com.lpu.AuthService.service;

import org.springframework.http.ResponseEntity;
import com.lpu.AuthService.dto.ForgotPasswordRequest;
import com.lpu.AuthService.dto.LoginRequest;
import com.lpu.AuthService.dto.RefreshTokenRequest;
import com.lpu.AuthService.dto.RegisterRequest;
import com.lpu.AuthService.dto.ResetPasswordRequest;
import com.lpu.AuthService.dto.response.AuthResponse;
import com.lpu.java.common_security.dto.ApiResponse;

public interface AuthService {

    ResponseEntity<ApiResponse<String>> sendOtp(String email);

    ResponseEntity<ApiResponse<String>> verifyOtp(String email, String otp);

    ResponseEntity<ApiResponse<String>> register(RegisterRequest req);

    ResponseEntity<ApiResponse<AuthResponse>> login(LoginRequest req);

    ResponseEntity<ApiResponse<AuthResponse>> refresh(RefreshTokenRequest request);

    ResponseEntity<ApiResponse<String>> forgotPassword(ForgotPasswordRequest request);

    ResponseEntity<ApiResponse<String>> resetPassword(ResetPasswordRequest request);
}