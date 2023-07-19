package com.voyage.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class RequestMethods {

    @Autowired
    private RestTemplate restTemplate;

    public String getUUID(String cookieValue) {
        String url="http://localhost:8060/api/security/getUUID";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", cookieValue);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        return response.getBody();
    }
}
