package com.ticket.Annotation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.WebUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Aspect
@Component
public class AuthorizeAspect {

    @Autowired
    private RestTemplate restTemplate;
    @Around("@annotation(Authorize)")
    public Object isUserHavePermission(ProceedingJoinPoint joinPoint) throws Throwable {
        Authorize authorize = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(Authorize.class);

        HttpServletRequest request = (HttpServletRequest) Arrays.stream(joinPoint.getArgs()).map(arg -> arg instanceof HttpServletRequest ? arg : null)
                .filter(Objects::nonNull).findFirst().orElseThrow(()-> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong."));

        Cookie cookie = WebUtils.getCookie(request, "Authorization");
        if (cookie == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cookie missing");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", cookie.getValue());

        String url = "http://localhost:8060/api/security/getAuthorities";
        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), List.class);
        boolean hasAuthority = response.getBody().stream().anyMatch(item -> item.equals(authorize.role().name()));

        if (Boolean.TRUE.equals(hasAuthority))
            return joinPoint.proceed();
        else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "not authorized to access ");
    }

}
