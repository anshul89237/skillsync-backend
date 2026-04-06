package com.lpu.NotificationService.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.lpu.NotificationService.service.EmailService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, String subject, String body) {
		logger.info("Attempting to send email to: {} with subject: '{}'", to, subject);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        try {
            mailSender.send(message);
			logger.info("Email successfully sent to: {}", to);
        } catch (Exception e) {
            logger.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }
}
