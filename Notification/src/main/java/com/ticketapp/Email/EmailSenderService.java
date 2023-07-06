package com.ticketapp.Email;

import com.ticketapp.Queue.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(Email email){
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(email.getTo());
        mailMessage.setFrom("talha.cakal2@gmail.com");
        mailMessage.setSubject(email.getSubject());
        mailMessage.setText(email.getBody());

        mailSender.send(mailMessage);
        log.info("Email Successfully Sent ID: " + email.getId());
    }


}
