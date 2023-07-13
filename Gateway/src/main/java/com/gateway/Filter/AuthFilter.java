package com.gateway.Filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
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
            System.out.println(exchange.getRequest().getCookies());
            System.out.println(exchange.getRequest().getPath());
            System.out.println("**********************");
//            ServerHttpRequest request = exchange.getRequest();
//            List<HttpCookie> cookies = exchange.getRequest().getCookies().get("Authorization");
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(HttpHeaders.COOKIE, cookies.get(0).toString());
//            HttpEntity<String> request = new HttpEntity<>(headers);
//            ResponseEntity<String> response = restTemplate.exchange("http://localhost:8060/api/security/validate", HttpMethod.GET, request, String.class);
//
//            System.out.println(response);

            return chain.filter(exchange);
        });
    }

    public static class Config{

    }
}
