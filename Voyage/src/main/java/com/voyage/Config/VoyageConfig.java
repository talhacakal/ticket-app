package com.voyage.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class VoyageConfig {

    @Bean
    public RestTemplate getTemplate(){
        return new RestTemplate();
    }
}
