package com.ticket.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TicketConfig {

    @Bean
    public RestTemplate getTemplate(){
        return new RestTemplate();
    }
}
