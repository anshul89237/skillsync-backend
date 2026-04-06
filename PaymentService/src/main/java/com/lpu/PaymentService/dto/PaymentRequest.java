package com.lpu.PaymentService.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private Long learnerId;
    private Long mentorId;
    private Long sessionId;
    private Double hours;
    private String paymentMethod;
}
