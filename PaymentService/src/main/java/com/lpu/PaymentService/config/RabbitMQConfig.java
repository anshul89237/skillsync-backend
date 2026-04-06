package com.lpu.PaymentService.config;

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

    // Existing Payment Queue
    @Bean
    public Queue paymentQueue() {
        return new Queue("payment.queue", true);
    }

    @Bean
    public DirectExchange paymentExchange() {
        return new DirectExchange("payment.exchange");
    }

    @Bean
    public Binding bindingPayment(Queue paymentQueue, DirectExchange paymentExchange) {
        return BindingBuilder.bind(paymentQueue).to(paymentExchange).with("payment.routingKey");
    }
    
    // Saga Queues and Exchange
    @Bean
    public DirectExchange sagaExchange() {
        return new DirectExchange("saga.exchange");
    }

    @Bean
    public Queue sessionSuccessQueue() {
        return new Queue("session.scheduled.success.queue", true);
    }

    @Bean
    public Queue sessionFailedQueue() {
        return new Queue("session.scheduled.failed.queue", true);
    }

    @Bean
    public Binding bindingSessionSuccess(Queue sessionSuccessQueue, DirectExchange sagaExchange) {
        return BindingBuilder.bind(sessionSuccessQueue).to(sagaExchange).with("session.success.routingKey");
    }

    @Bean
    public Binding bindingSessionFailed(Queue sessionFailedQueue, DirectExchange sagaExchange) {
        return BindingBuilder.bind(sessionFailedQueue).to(sagaExchange).with("session.failed.routingKey");
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
