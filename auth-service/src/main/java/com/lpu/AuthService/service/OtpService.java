package com.lpu.AuthService.service;

public interface OtpService {

    String generateOtp();

    void saveOtp(String email, String otp);

    void checkCooldown(String email);

    void deleteExpiredOtps();

    void verifyOtp(String email, String otp);

    void markVerified(String email);

    boolean isOtpVerified(String email);
}
