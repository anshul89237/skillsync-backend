package com.lpu.SessionService.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lpu.SessionService.dto.SagaPaymentEvent;
import com.lpu.SessionService.dto.SagaSessionEvent;
import com.lpu.SessionService.entity.Session;
import com.lpu.SessionService.repository.SessionRepository;
import com.lpu.SessionService.producer.SessionSagaProducer;

@Component
public class PaymentSagaConsumer {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SessionSagaProducer sagaProducer;

    @RabbitListener(queues = "payment.requested.queue")
    public void handlePaymentRequested(SagaPaymentEvent event) {
        try {
            Session session = sessionRepository.findById(event.getSessionId()).orElse(null);
            
            if (session != null) {
                // If it successfully finds the session, schedule it
                session.setStatus("SCHEDULED");
                sessionRepository.save(session);
                
                // Publish SUCCESS to Saga Exchange
                SagaSessionEvent successEvent = new SagaSessionEvent(
                        event.getPaymentId(),
                        session.getId(),
                        "SUCCESS"
                );
                sagaProducer.sendSuccessEvent(successEvent);
                
            } else {
                // Session not found (maybe deleted or invalid) -> rollback payment
                SagaSessionEvent failedEvent = new SagaSessionEvent(
                        event.getPaymentId(),
                        event.getSessionId(),
                        "FAILED"
                );
                sagaProducer.sendFailedEvent(failedEvent);
            }
        } catch (Exception e) {
            SagaSessionEvent failedEvent = new SagaSessionEvent(
                    event.getPaymentId(),
                    event.getSessionId(),
                    "FAILED"
            );
            sagaProducer.sendFailedEvent(failedEvent);
        }
    }
}
