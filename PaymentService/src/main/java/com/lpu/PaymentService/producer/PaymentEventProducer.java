package com.lpu.PaymentService.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lpu.PaymentService.dto.PaymentEvent;

@Service
public class PaymentEventProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendPaymentEvent(PaymentEvent event) {
        rabbitTemplate.convertAndSend(
                "payment.exchange",
                "payment.routingKey",
                event
        );
    }

    public void sendSagaEvent(com.lpu.PaymentService.dto.SagaPaymentEvent event) {
        rabbitTemplate.convertAndSend(
                "saga.exchange",
                "payment.requested.routingKey",
                event
        );
    }
}
