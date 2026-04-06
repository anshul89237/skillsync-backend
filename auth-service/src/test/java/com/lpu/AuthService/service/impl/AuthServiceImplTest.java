package com.lpu.AuthService.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.lpu.AuthService.dto.LoginRequest;
import com.lpu.AuthService.dto.RegisterRequest;
import com.lpu.AuthService.dto.response.AuthResponse;
import com.lpu.AuthService.entity.User;
import com.lpu.AuthService.repository.AuthMapper;
import com.lpu.AuthService.repository.AuthUserRepository;
import com.lpu.AuthService.service.EmailService;
import com.lpu.AuthService.service.OtpService;
import com.lpu.AuthService.service.RefreshTokenService;
import com.lpu.java.common_security.config.JwtUtil;
import com.lpu.java.common_security.dto.ApiResponse;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthUserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private OtpService otpService;

    @Mock
    private EmailService emailService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private AuthMapper authMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("Password@123");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("Password@123");

        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setRole("ROLE_USER");
        user.getProviders().add("LOCAL");
    }

    @Test
    void register_ShouldReturnSuccessResponse() {
        when(otpService.isOtpVerified(anyString())).thenReturn(true);
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(authMapper.toUser(any(RegisterRequest.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(repository.save(any(User.class))).thenReturn(user);

        ResponseEntity<ApiResponse<String>> responseEntity = authService.register(registerRequest);
        ApiResponse<String> response = responseEntity.getBody();

        assertNotNull(response);
        assertEquals("User registered successfully", response.getMessage());
        verify(repository).save(any(User.class));
    }

    @Test
    void login_ShouldReturnAuthResponse_WhenCredentialsAreValid() {
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(any(), any(), any(), any())).thenReturn("testToken");
        
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken("testToken");
        when(authMapper.toAuthResponse(anyString(), any())).thenReturn(authResponse);

        ResponseEntity<ApiResponse<AuthResponse>> responseEntity = authService.login(loginRequest);
        ApiResponse<AuthResponse> response = responseEntity.getBody();

        assertNotNull(response);
        assertEquals("Login successful", response.getMessage());
        assertNotNull(response.getData().getAccessToken());
    }

    @Test
    void login_ShouldThrowException_WhenUserNotFound() {
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
    }
}
