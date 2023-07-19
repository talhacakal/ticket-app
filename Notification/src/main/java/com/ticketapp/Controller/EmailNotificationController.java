package com.ticketapp.Controller;

import com.ticketapp.Queue.Email;
import com.ticketapp.Queue.EmailProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/email")
public class EmailNotificationController {

    @Autowired
    private EmailProducer emailProducer;

    @GetMapping("")
    public String greeting(){
        return "sa";
    }
    @PostMapping("")
    public void sendEmail(@RequestBody Email email){
        log.info("Email received ID:"+email.getId());
        this.emailProducer.sendToQueue(email);
    }
}
