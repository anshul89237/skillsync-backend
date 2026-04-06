package com.lpu.PaymentService.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lpu.PaymentService.dto.PaymentEvent;
import com.lpu.PaymentService.dto.SagaSessionEvent;
import com.lpu.PaymentService.entity.Payment;
import com.lpu.PaymentService.producer.PaymentEventProducer;
import com.lpu.PaymentService.repository.PaymentRepository;

@Component
public class SessionSagaConsumer {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentEventProducer producer;

    @RabbitListener(queues = "session.scheduled.success.queue")
    public void handleSessionSuccess(SagaSessionEvent event) {
        Payment payment = paymentRepository.findById(event.getPaymentId()).orElse(null);
        if (payment != null && "PENDING".equals(payment.getStatus())) {
            payment.setStatus("SUCCESS");
            paymentRepository.save(payment);

            // Finally trigger the original Notification Email PaymentEvent
            PaymentEvent notificationEvent = new PaymentEvent(
                    payment.getId(),
                    payment.getLearnerId(),
                    payment.getMentorId(),
                    payment.getSessionId(),
                    payment.getTotalAmount(),
                    payment.getStatus(),
                    payment.getTransactionId(),
                    payment.getPaidAt()
            );
            producer.sendPaymentEvent(notificationEvent);
        }
    }

    @RabbitListener(queues = "session.scheduled.failed.queue")
    public void handleSessionFailed(SagaSessionEvent event) {
        Payment payment = paymentRepository.findById(event.getPaymentId()).orElse(null);
        if (payment != null && "PENDING".equals(payment.getStatus())) {
            payment.setStatus("FAILED");
            // Here you might trigger a Refund Logic
            paymentRepository.save(payment);
        }
    }
}
