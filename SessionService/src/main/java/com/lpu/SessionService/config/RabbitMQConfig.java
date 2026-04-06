package com.lpu.SessionService.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Existing Session queues
    @Bean
    public Queue sessionQueue() {
        return new Queue("session.queue", true);
    }

    @Bean
    public DirectExchange sessionExchange() {
        return new DirectExchange("session.exchange");
    }

    @Bean
    public Binding bindingSession(Queue sessionQueue, DirectExchange sessionExchange) {
        return BindingBuilder.bind(sessionQueue).to(sessionExchange).with("session.routingKey");
    }

    // Saga Configurations
    @Bean
    public DirectExchange sagaExchange() {
        return new DirectExchange("saga.exchange");
    }

    @Bean
    public Queue paymentRequestedQueue() {
        return new Queue("payment.requested.queue", true);
    }

    @Bean
    public Binding bindingPaymentRequested(Queue paymentRequestedQueue, DirectExchange sagaExchange) {
        return BindingBuilder.bind(paymentRequestedQueue).to(sagaExchange).with("payment.requested.routingKey");
    }

    @Bean
    public MessageConverter messageConverter() {
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
