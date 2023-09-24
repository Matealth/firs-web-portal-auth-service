package com.firs.risk.mgt.auth.svc.service;

import com.firs.risk.mgt.auth.svc.model.OTP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMQService {

    @Value("${rabbitmq.exchange.name}")
    public String AUTH_EXCHANGE_NAME;

    @Value("${otp.rabbitmq.routing.key}")
    public String OTP_ROUTING_KEY;

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendOTP(final OTP otp) {
        amqpTemplate.convertAndSend(AUTH_EXCHANGE_NAME, OTP_ROUTING_KEY, otp);
    }

}