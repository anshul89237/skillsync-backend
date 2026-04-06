package com.lpu.AuthService.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lpu.AuthService.custom.exceptions.InvalidCredentialsException;
import com.lpu.AuthService.dto.LoginRequest;
import com.lpu.AuthService.dto.RefreshTokenRequest;
import com.lpu.AuthService.dto.RegisterRequest;
import com.lpu.AuthService.dto.response.AuthResponse;
import com.lpu.AuthService.service.AuthService;
import com.lpu.java.common_security.config.JwtUtil;
import com.lpu.java.common_security.dto.ApiResponse;

@WebMvcTest(AuthController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
@Import(JwtUtil.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtUtil jwtUtil; // Required because of security filter dependency

    @Autowired
    private ObjectMapper objectMapper;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");
        registerRequest.setEmail("john@example.com");
        registerRequest.setPassword("Password123!");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("john@example.com");
        loginRequest.setPassword("Password123");
    }

    @Test
    void shouldReturn200_whenSendOtpWithValidEmail() throws Exception {
        when(authService.sendOtp(anyString())).thenReturn(ResponseEntity.ok(ApiResponse.success("OTP sent", null)));

        mockMvc.perform(post("/api/v1/auth/send-otp")
                .param("email", "john@example.com"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn400_whenSendOtpWithInvalidEmail() throws Exception {
        mockMvc.perform(post("/api/v1/auth/send-otp")
                .param("email", "not-an-email"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn200_whenRegisterWithValidData() throws Exception {
        when(authService.register(any(RegisterRequest.class)))
                .thenReturn(ResponseEntity.ok(ApiResponse.success("Registered", null)));

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn400_whenRegisterWithInvalidPassword() throws Exception {
        registerRequest.setPassword("123"); // Too short according to common patterns

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn200_whenLoginWithValidCredentials() throws Exception {
        AuthResponse response = new AuthResponse();
        response.setAccessToken("token");
        response.setRefreshToken("refresh");

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(ResponseEntity.ok(ApiResponse.success("Login Success", response)));

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value("token"));
    }

    @Test
    void shouldReturn200_whenRefreshWithValidToken() throws Exception {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("this_is_a_very_valid_refresh_token_string");
        
        AuthResponse response = new AuthResponse();
        response.setAccessToken("new_token");

        when(authService.refresh(any(RefreshTokenRequest.class)))
                .thenReturn(ResponseEntity.ok(ApiResponse.success("Refreshed", response)));

        mockMvc.perform(post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}