package com.ticket.Config;

import com.ticket.Entity.DTO.VoyageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class RequestMethods {

    @Autowired
    private RestTemplate restTemplate;

    public VoyageDTO getVoyage(String voyageUID) {
        String url = "http://localhost:8070/api/voyage/" + voyageUID;
        ResponseEntity<VoyageDTO> response = restTemplate.exchange(url, HttpMethod.GET, null, VoyageDTO.class);

        return response.getBody();
    }
    public ResponseEntity getSeat(String voyageUID, int seatNo, int gender) {
        String url = "http://localhost:8070/api/seat?voyageUID=" + voyageUID + "&seatNo=" + seatNo + "&gender=" + gender;
        ResponseEntity response =  restTemplate.exchange(url, HttpMethod.POST, null, String.class);

        return response;
    }
    public HttpStatusCode getPaymantStatus(int amount, String paymentType) {
        String url = "http://localhost:8090/api/payment?amount=" + amount + "&paymentType=" + paymentType;
        return restTemplate.exchange(url, HttpMethod.POST, null, Void.class).getStatusCode();
    }
    public int getTicketPrice(String voyageUID){
        String url="http://localhost:8070/api/voyage?voyageUUID="+voyageUID;
        ResponseEntity<Integer> response = restTemplate.exchange(url, HttpMethod.GET, null, Integer.class);
        return response.getBody();
    }
    public List getAuthorities(String cookieValue){
        String url="http://localhost:8060/api/security/getAuthorities";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", cookieValue);

        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), List.class);
        return response.getBody();
    }
    public String getUUID(String cookieValue) {
        String url="http://localhost:8060/api/security/getUUID";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", cookieValue);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        return response.getBody();
    }
}
