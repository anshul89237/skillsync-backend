package com.lpu.SessionService.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lpu.SessionService.dto.SessionEvent;

@Service
public class SessionEventProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendSessionEvent(SessionEvent event) {
        rabbitTemplate.convertAndSend(
                "session.exchange",
                "session.routingKey",
                event
        );
    }
}