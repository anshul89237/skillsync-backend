package com.lpu.AuthService.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lpu.AuthService.custom.exceptions.InvalidCredentialsException;
import com.lpu.AuthService.custom.exceptions.OtpNotVerifiedException;
import com.lpu.AuthService.custom.exceptions.UserAlreadyExistsException;
import com.lpu.AuthService.custom.exceptions.UserNotFoundException;
import com.lpu.AuthService.dto.ForgotPasswordRequest;
import com.lpu.AuthService.dto.LoginRequest;
import com.lpu.AuthService.dto.RefreshTokenRequest;
import com.lpu.AuthService.dto.RegisterRequest;
import com.lpu.AuthService.dto.ResetPasswordRequest;
import com.lpu.AuthService.dto.response.AuthResponse;
import com.lpu.AuthService.entity.RefreshToken;
import com.lpu.AuthService.entity.User;
import com.lpu.AuthService.repository.AuthMapper;
import com.lpu.AuthService.repository.AuthUserRepository;
import com.lpu.AuthService.service.AuthService;
import com.lpu.AuthService.service.EmailService;
import com.lpu.AuthService.service.OtpService;
import com.lpu.AuthService.service.RefreshTokenService;
import com.lpu.java.common_security.dto.ApiResponse;
import com.lpu.java.common_security.config.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final AuthUserRepository repo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager manager;
    private final JwtUtil jwtUtil;
    private final OtpService otpService;
    private final EmailService emailService;
    private final RefreshTokenService refreshTokenService;
    private final AuthMapper mapper;

    @Override
    public ResponseEntity<ApiResponse<String>> sendOtp(String email) {
		logger.info("Initiating OTP send process for email: {}", email);
        otpService.checkCooldown(email);
        String otp = otpService.generateOtp();
        otpService.saveOtp(email, otp);
        emailService.sendOtpEmail(email, otp);
		logger.info("OTP generated and sent successfully to email: {}", email);
        return ResponseEntity.ok(ApiResponse.success("OTP sent to email", null));
    }

    @Override
    public ResponseEntity<ApiResponse<String>> verifyOtp(String email, String otp) {
		logger.info("Verifying OTP for email: {}", email);
        otpService.verifyOtp(email, otp);
        otpService.markVerified(email);
		logger.info("OTP verification successful for email: {}", email);
        return ResponseEntity.ok(ApiResponse.success("OTP verified successfully", null));
    }

    @Override
    public ResponseEntity<ApiResponse<String>> register(RegisterRequest req) {
		logger.info("Registering new user with email: {}", req.getEmail());
        if (repo.findByEmail(req.getEmail()).isPresent()) {
			logger.warn("Registration failed: User with email {} already exists", req.getEmail());
            throw new UserAlreadyExistsException("User already exists");
        }
        User user = mapper.toUser(req);
        user.setPassword(encoder.encode(req.getPassword()));
        user.setVerified(true);
        user.getProviders().add("LOCAL");
        User savedUser = repo.save(user);
		logger.info("User registered successfully with ID: {} and email: {}", savedUser.getId(), req.getEmail());
        return ResponseEntity.ok(ApiResponse.success("User registered successfully", null));
    }

    @Override
    public ResponseEntity<ApiResponse<AuthResponse>> login(LoginRequest req) {
		logger.info("Processing login for email: {}", req.getEmail());
        User user = repo.findByEmail(req.getEmail())
                .orElseThrow(() -> {
					logger.error("Login failed: User with email {} not registered", req.getEmail());
					return new UserNotFoundException("USER NOT REGISTERED");
				});
        if (!user.getProviders().contains("LOCAL")) {
			logger.warn("Login failed: User with email {} attempted local login but is a Google user", req.getEmail());
            throw new InvalidCredentialsException("Please login using Google");
        }
        try {
            manager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        } catch (Exception e) {
			logger.error("Login failed: Invalid credentials for email: {}", req.getEmail());
            throw new InvalidCredentialsException("Invalid email or password");
        }
        String accessToken = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole(), user.getName());
        RefreshToken refreshToken = refreshTokenService.createToken(user.getEmail());
        AuthResponse response = mapper.toAuthResponse(accessToken, refreshToken);
		logger.info("Login successful for email: {}. JWT generated.", req.getEmail());
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @Override
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(RefreshTokenRequest request) {
		logger.info("Processing token refresh request");
        RefreshToken newToken = refreshTokenService.rotateToken(request.getRefreshToken());
        User user = newToken.getUser();
		logger.info("RefreshToken rotated for user: {}", user.getEmail());
        String newAccessToken = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole(), user.getName());
        AuthResponse response = mapper.toAuthResponse(newAccessToken, newToken);
		logger.info("JWT AccessToken refreshed successfully for user: {}", user.getEmail());
        return ResponseEntity.ok(ApiResponse.success("Token refreshed", response));
    }

    @Override
    public ResponseEntity<ApiResponse<String>> forgotPassword(ForgotPasswordRequest request) {
        logger.info("Forgot password request for email: {}", request.getEmail());
        repo.findByEmail(request.getEmail())
            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));
        
        // Use existing sendOtp logic
        return sendOtp(request.getEmail());
    }

    @Override
    public ResponseEntity<ApiResponse<String>> resetPassword(ResetPasswordRequest request) {
        logger.info("Resetting password for email: {}", request.getEmail());
        if (!otpService.isOtpVerified(request.getEmail())) {
            logger.error("Reset password failed: OTP not verified for email: {}", request.getEmail());
            throw new OtpNotVerifiedException("Please verify OTP before resetting password");
        }

        User user = repo.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));

        user.setPassword(encoder.encode(request.getNewPassword()));
        repo.save(user);

        // Optional: Invalidate OTP session if needed (markVerified flag will expire in 10 mins anyway)
        logger.info("Password reset successful for email: {}", request.getEmail());
        return ResponseEntity.ok(ApiResponse.success("Password reset successfully", null));
    }
}
