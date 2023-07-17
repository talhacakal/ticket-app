package com.ticket.Controller;

import com.ticket.Config.RequestMethods;
import com.ticket.Config.TicketTotalDTO;
import com.ticket.Entity.DTO.TicketDTO;
import com.ticket.Entity.DTO.TicketItem;
import com.ticket.Entity.DTO.TicketSaleDTO;
import com.ticket.Entity.Ticket;
import com.ticket.Repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketRepository ticketRepository;
    private final RequestMethods requestMethods;

    @GetMapping(params = {"UUID"})
    public ResponseEntity<List<TicketDTO>> getAllTickets(@RequestParam String UUID) {
        List<Ticket> ticketList = this.ticketRepository.findByUUID(UUID);
        if (ticketList.size() == 0) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(
                ticketList.stream()
                        .map(item -> new TicketDTO(item, requestMethods.getVoyage(item.getVoyageUID())))
                        .toList());
    }
    @GetMapping("/total")   //TODO: only admin
    public ResponseEntity<TicketTotalDTO> getTotal(@RequestParam String voyageUID){
        List<Ticket> list = this.ticketRepository.findByVoyageUID(voyageUID);
        int totalAmount= list.stream().mapToInt(Ticket::getPrice).sum();
        return ResponseEntity.ok(
                new TicketTotalDTO(
                        list.size(),
                        totalAmount
                )
        );
    }
    @PostMapping("")
    public ResponseEntity ticketSale(@RequestBody TicketSaleDTO ticketSaleDTO) {
        String UUID="36dec986-5070-4c72-b031-223dc174756e"; //TODO: Add authentication
        List<Ticket> tickets=new ArrayList<>();
        int ticketPrice = requestMethods.getTicketPrice(ticketSaleDTO.getVoyageUID());
        for(TicketItem ticket : ticketSaleDTO.getTicketItems()){
            ResponseEntity seatStatus = requestMethods.getSeat(ticketSaleDTO.getVoyageUID(), ticket.getSeatNo(), ticket.getGender());
            if (seatStatus.getStatusCode() != HttpStatus.OK) return seatStatus;

            if (requestMethods.getPaymantStatus(ticketPrice, ticketSaleDTO.getPaymentType()) != HttpStatus.OK)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment Error");

            Ticket newTicket = Ticket.builder()
                    .ticketUID(java.util.UUID.randomUUID().toString())
                    .UUID(UUID)
                    .voyageUID(ticketSaleDTO.getVoyageUID())
                    .name(ticket.getName())
                    .surname(ticket.getSurname())
                    .gender(ticket.getGender())
                    .price(ticketPrice)
                    .seatNo(ticket.getSeatNo())
                    .createDate(LocalDateTime.now()).build();

            var savedTicket = this.ticketRepository.save(newTicket);
            tickets.add(savedTicket);
        }

        return ResponseEntity.ok(tickets);
    }

}
