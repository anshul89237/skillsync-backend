package com.lpu.PaymentService.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Index;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments", indexes = {
    @Index(name = "idx_payment_learner_id", columnList = "learnerId"),
    @Index(name = "idx_payment_mentor_id", columnList = "mentorId"),
    @Index(name = "idx_payment_session_id", columnList = "sessionId"),
    @Index(name = "idx_payment_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long learnerId;
	private Long mentorId;
	private Long sessionId;
	private Double hours;
	private Double hourlyRate;
	private Double totalAmount;
	private String paymentMethod;
	private String status;
	private String transactionId;
	private LocalDateTime paidAt;

	// Razorpay specific fields
	private String razorpayOrderId;
	private String razorpayPaymentId;
	private String razorpaySignature;
}
