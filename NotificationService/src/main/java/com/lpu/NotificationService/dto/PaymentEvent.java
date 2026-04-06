package com.lpu.NotificationService.dto;

import java.time.LocalDateTime;

public class PaymentEvent {
    private Long paymentId;
    private Long learnerId;
    private Long mentorId;
    private Long sessionId;
    private Double totalAmount;
    private String status;
    private String transactionId;
    private LocalDateTime paidAt;

	public PaymentEvent() {}

	public Long getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}
	public Long getLearnerId() {
		return learnerId;
	}
	public void setLearnerId(Long learnerId) {
		this.learnerId = learnerId;
	}
	public Long getMentorId() {
		return mentorId;
	}
	public void setMentorId(Long mentorId) {
		this.mentorId = mentorId;
	}
	public Long getSessionId() {
		return sessionId;
	}
	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public LocalDateTime getPaidAt() {
		return paidAt;
	}
	public void setPaidAt(LocalDateTime paidAt) {
		this.paidAt = paidAt;
	}
}
