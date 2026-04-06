package com.lpu.PaymentService.controller;
 
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lpu.PaymentService.dto.PaymentRequest;
import com.lpu.PaymentService.entity.Payment;
import com.lpu.PaymentService.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<Payment> processPayment(@RequestBody PaymentRequest request) {
		logger.info("Payment processing request received for Session ID: {} - Hours: {}", request.getSessionId(), request.getHours());
        return ResponseEntity.status(201).body(paymentService.processPayment(request));
    }

    @PostMapping("/verify")
    public ResponseEntity<Payment> verifyPayment(@RequestBody Map<String, String> responseData) {
        logger.info("Payment verification request received for Razorpay Order ID: {}", responseData.get("razorpay_order_id"));
        return ResponseEntity.ok(paymentService.verifyRazorpayPayment(responseData));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> findPaymentById(@PathVariable Long id) {
		logger.info("Request to fetch payment by ID: {}", id);
        return ResponseEntity.ok(paymentService.findPaymentById(id));
    }

    @GetMapping("/learner/{learnerId}")
    public ResponseEntity<List<Payment>> findPaymentsByLearnerId(@PathVariable Long learnerId) {
		logger.info("Request to fetch payments for Learner ID: {}", learnerId);
        return ResponseEntity.ok(paymentService.findPaymentsByLearnerId(learnerId));
    }

    @GetMapping("/mentor/{mentorId}")
    public ResponseEntity<List<Payment>> findPaymentsByMentorId(@PathVariable Long mentorId) {
		logger.info("Request to fetch payments for Mentor ID: {}", mentorId);
        return ResponseEntity.ok(paymentService.findPaymentsByMentorId(mentorId));
    }

    @GetMapping
    public ResponseEntity<List<Payment>> findAllPayments() {
		logger.info("Request to fetch all payments");
        return ResponseEntity.ok(paymentService.findAllPayments());
    }
}
