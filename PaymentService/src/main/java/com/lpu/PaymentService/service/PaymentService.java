package com.lpu.PaymentService.service;

import java.util.List;
import java.util.Map;

import com.lpu.PaymentService.dto.PaymentRequest;
import com.lpu.PaymentService.entity.Payment;

public interface PaymentService {

    Payment processPayment(PaymentRequest request);

    Payment verifyRazorpayPayment(Map<String, String> responseData);

    Payment findPaymentById(Long id);

    List<Payment> findPaymentsByLearnerId(Long learnerId);

    List<Payment> findPaymentsByMentorId(Long mentorId);

    List<Payment> findAllPayments();
}
