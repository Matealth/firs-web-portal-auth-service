package com.firs.risk.mgt.auth.svc.config;

import org.springframework.amqp;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.name}")
    public String AUTH_EXCHANGE_NAME;

    @Value("${otp.rabbitmq.routing.key}")
    public String OTP_ROUTING_KEY;

    @Value("${otp.rabbitmq.queue.name}")
    public String OTP_QUEUE_NAME;

    @Bean
    DirectExchange authExchange() {
        return new DirectExchange(AUTH_EXCHANGE_NAME);
    }

    @Bean
    Queue otpQueue() {
        return QueueBuilder
                .durable(OTP_QUEUE_NAME)
                .build();
    }

    @Bean
    Binding otpBinding() {
        return BindingBuilder
                .bind(otpQueue()).to(authExchange())
                .with(OTP_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
