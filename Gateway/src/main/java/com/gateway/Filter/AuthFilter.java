package com.gateway.Filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    @Autowired
    private RestTemplate restTemplate;
    public AuthFilter(){
        super(Config.class);
    }
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            HttpHeaders headers = new HttpHeaders();
            List<HttpCookie> cookies = exchange.getRequest().getCookies().get("Authorization");

            if (!cookies.isEmpty())
                cookies.forEach(item -> headers.set(item.getName(),item.getValue()));
            else
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cookie is missing");

            ResponseEntity response = restTemplate.exchange("http://localhost:8060/api/security/validate", HttpMethod.GET, new HttpEntity<String>(headers), ResponseEntity.class);
            if (response.getStatusCode() == HttpStatus.OK)
                return chain.filter(exchange);
            else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cookie is not valid");
        });
    }

    public static class Config{

    }
}
