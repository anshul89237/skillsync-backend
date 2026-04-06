package com.lpu.SessionService.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lpu.SessionService.dto.SagaSessionEvent;

@Service
public class SessionSagaProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendSuccessEvent(SagaSessionEvent event) {
        rabbitTemplate.convertAndSend(
                "saga.exchange",
                "session.success.routingKey",
                event
        );
    }

    public void sendFailedEvent(SagaSessionEvent event) {
        rabbitTemplate.convertAndSend(
                "saga.exchange",
                "session.failed.routingKey",
                event
        );
    }
}
