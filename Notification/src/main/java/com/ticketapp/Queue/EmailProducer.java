package com.ticketapp.Queue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailProducer {

    @Value("${sr.rabbit.routing.name}")
    private String routingName;
    @Value("${sr.rabbit.exchange.name}")
    private String exchangeName;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void sendToQueue(Email email){
        rabbitTemplate.convertAndSend(exchangeName, routingName, email);
        log.info("Email send to queue ID: " + email.getId());
    }

}
