package com.ticket.Config;

import com.ticket.Entity.DTO.VoyageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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

}
