package com.lpu.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lpu.AuthService.service.OtpService;

@Component
public class OtpCleanupSchedular {

    @Autowired
    private OtpService otpService;

    @Scheduled(fixedRate = 600000)  // 10 mins
    public void cleanup() {
        otpService.deleteExpiredOtps();
        System.out.println("🧹 Expired OTPs cleaned");
    }
}