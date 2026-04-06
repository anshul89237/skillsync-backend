package com.lpu.AuthService.service;

public interface EmailService {

    void sendOtpEmail(String to, String otp);
}
