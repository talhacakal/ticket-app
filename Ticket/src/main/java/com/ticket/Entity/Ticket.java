package com.ticket.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Getter
@Setter
@ToString
@Builder
public class Ticket {

    @Id
    private String id;
    private String ticketUID;
    private String UUID;
    private String voyageUID;
    private String name;
    private String surname;
    private int gender;
    private int price;
    private int seatNo;
    private LocalDateTime createDate;
}
