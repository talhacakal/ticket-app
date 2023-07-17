package com.ticket.Entity.DTO;

import com.ticket.Entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {

    private String ticketUID;
    private VoyageDTO voyageDTO;
    private String username;
    private String surname;
    private int price;
    private int seatNo;
    private LocalDateTime createDate;


    public TicketDTO(Ticket ticket, VoyageDTO voyageDTO) {
        this.ticketUID = ticket.getTicketUID();
        this.voyageDTO = voyageDTO;
        this.username = ticket.getName();
        this.surname = ticket.getSurname();
        this.price = ticket.getPrice();
        this.seatNo = ticket.getSeatNo();
        this.createDate = ticket.getCreateDate();
    }
}
