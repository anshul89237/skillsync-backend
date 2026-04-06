package com.lpu.NotificationService.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Test
    void shouldSendEmail_Success() {
        // when
        emailService.sendEmail("test@example.com", "Subject", "Body");

        // then
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void shouldHandleException_whenMailSenderFails() {
        // given
        doThrow(new RuntimeException("SMTP Error")).when(mailSender).send(any(SimpleMailMessage.class));

        // when
        emailService.sendEmail("test@example.com", "Subject", "Body");

        // then
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        // No exception thrown as it is caught inside the service
    }
}
