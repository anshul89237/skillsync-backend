package com.lpu.NotificationService.service;

public interface EmailService {

    void sendEmail(String to, String subject, String body);
}
