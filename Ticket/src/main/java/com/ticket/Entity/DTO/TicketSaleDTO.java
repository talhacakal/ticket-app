package com.ticket.Entity.DTO;

import lombok.Data;

import java.util.List;

@Data
public class TicketSaleDTO {
    private String paymentType;
    private String voyageUID;
    private List<TicketItem> ticketItems;
}
