package com.gateway.Filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    public AuthFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            System.out.println("Auth Path" + exchange.getRequest().getPath());
            System.out.println("Auth Cookie" + exchange.getRequest().getCookies());
            System.out.println("Auth Method" + exchange.getRequest().getMethod());

            log.info("Auth Filter");

            return chain.filter(exchange);
        });
    }

    public static class Config{

    }
}
