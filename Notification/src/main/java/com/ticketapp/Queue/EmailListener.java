package com.ticketapp.Queue;

import com.ticketapp.Email.EmailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailListener {

    @Autowired
    private EmailSenderService emailSenderService;

    @RabbitListener(queues = "email-queue")
    public void handleMessage(Email email){
        log.info("Email received ID: "+email.getId());

        this.emailSenderService.sendEmail(email);

        log.info("Email send: "+email.getId());
    }

}
