package com.lpu.PaymentService.dto;

public class SagaPaymentEvent {
    private Long paymentId;
    private Long sessionId;
    private String status;

    public SagaPaymentEvent() {}

    public SagaPaymentEvent(Long paymentId, Long sessionId, String status) {
        this.paymentId = paymentId;
        this.sessionId = sessionId;
        this.status = status;
    }

    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
