package com.lpu.AuthService.service.impl;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.lpu.AuthService.custom.exceptions.InvalidOtpException;
import com.lpu.AuthService.custom.exceptions.OtpAttemptsExceededException;
import com.lpu.AuthService.custom.exceptions.OtpNotFoundException;
import com.lpu.AuthService.service.OtpService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final StringRedisTemplate redisTemplate;

    private static final String OTP_KEY_PREFIX = "otp:";
    private static final String COOLDOWN_KEY_PREFIX = "cooldown:";
    private static final String VERIFIED_KEY_PREFIX = "verified:";
    private static final String ATTEMPTS_KEY_PREFIX = "attempts:";

    @Override
    public String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    @Override
    public void saveOtp(String email, String otp) {
        // Store OTP with 5 minutes TTL
        redisTemplate.opsForValue().set(OTP_KEY_PREFIX + email, otp, 5, TimeUnit.MINUTES);
        
        // Reset attempts for this email
        redisTemplate.delete(ATTEMPTS_KEY_PREFIX + email);
        
        // Set cooldown for 30 seconds
        redisTemplate.opsForValue().set(COOLDOWN_KEY_PREFIX + email, "true", 30, TimeUnit.SECONDS);

        // Ensure verified flag is cleared when new OTP is sent
        redisTemplate.delete(VERIFIED_KEY_PREFIX + email);
    }

    @Override
    public void checkCooldown(String email) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(COOLDOWN_KEY_PREFIX + email))) {
            throw new OtpAttemptsExceededException("Wait 30 seconds before requesting OTP again");
        }
    }

    @Override
    public void deleteExpiredOtps() {
        // Redis handles expiration automatically via TTL
    }

    @Override
    public void verifyOtp(String email, String otp) {
        String storedOtp = redisTemplate.opsForValue().get(OTP_KEY_PREFIX + email);

        if (storedOtp == null) {
            throw new OtpNotFoundException("OTP expired or not found. Please request a new one.");
        }

        String attemptsStr = redisTemplate.opsForValue().get(ATTEMPTS_KEY_PREFIX + email);
        int attempts = attemptsStr != null ? Integer.parseInt(attemptsStr) : 0;

        if (attempts >= 3) {
            throw new OtpAttemptsExceededException("Maximum attempts reached");
        }

        if (!storedOtp.equals(otp)) {
            redisTemplate.opsForValue().increment(ATTEMPTS_KEY_PREFIX + email);
            throw new InvalidOtpException("Invalid OTP entered");
        }

        // Mark as verified in Redis
        markVerified(email);
        
        // Delete OTP and attempts after successful verification
        redisTemplate.delete(OTP_KEY_PREFIX + email);
        redisTemplate.delete(ATTEMPTS_KEY_PREFIX + email);
    }

    @Override
    public void markVerified(String email) {
        redisTemplate.opsForValue().set(VERIFIED_KEY_PREFIX + email, "true", 10, TimeUnit.MINUTES);
    }

    @Override
    public boolean isOtpVerified(String email) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(VERIFIED_KEY_PREFIX + email));
    }
}
