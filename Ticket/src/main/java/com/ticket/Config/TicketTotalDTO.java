package com.ticket.Config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
public class TicketTotalDTO {
    private int totalTicket;
    private int totalAmount;
}
