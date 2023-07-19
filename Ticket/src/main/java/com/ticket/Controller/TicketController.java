package com.ticket.Controller;

import com.ticket.Annotation.Authorize;
import com.ticket.Annotation.Role;
import com.ticket.Config.RequestMethods;
import com.ticket.Entity.DTO.TicketTotalDTO;
import com.ticket.Entity.DTO.TicketDTO;
import com.ticket.Entity.DTO.TicketItem;
import com.ticket.Entity.DTO.TicketSaleDTO;
import com.ticket.Entity.Ticket;
import com.ticket.Repository.TicketRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketRepository ticketRepository;
    private final RequestMethods requestMethods;

    @GetMapping("")
    public ResponseEntity<List<TicketDTO>> getAllTickets(HttpServletRequest request) {
        String UUID = requestMethods.getUUID(WebUtils.getCookie(request, "Authorization").getValue());
        List<Ticket> ticketList = this.ticketRepository.findByUUID(UUID);
        if (ticketList.size() == 0) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(
                ticketList.stream()
                        .map(item -> new TicketDTO(item, requestMethods.getVoyage(item.getVoyageUID())))
                        .toList());
    }
    @GetMapping("/total")
    @Authorize(role = Role.ROLE_ADMIN)
    public ResponseEntity<TicketTotalDTO> getTotal(HttpServletRequest httpServletRequest, @RequestParam String voyageUID){
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
    public ResponseEntity ticketSale(HttpServletRequest request, @RequestBody TicketSaleDTO ticketSaleDTO) {
        List<String> authorities = requestMethods.getAuthorities(WebUtils.getCookie(request,"Authorization").getValue());
        String UUID = requestMethods.getUUID(WebUtils.getCookie(request,"Authorization").getValue());

        if (authorities.stream().anyMatch(item -> item.equals(Role.ROLE_INSTITUTIONAL.name()))) {
            int ticketCount = this.ticketRepository.findByUUIDAndVoyageUID(UUID, ticketSaleDTO.getVoyageUID()).size() + ticketSaleDTO.getTicketItems().size();
            if (ticketSaleDTO.getTicketItems().size() > 20 ||
                    ( ticketCount > 20))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("max ticket limet 20");
        }
        else if (authorities.stream().anyMatch(item -> item.equals(Role.ROLE_INDIVIDUAL.name()))) {
            if (ticketSaleDTO.getTicketItems().size() > 5 ||
                    (this.ticketRepository.findByUUIDAndVoyageUID(UUID, ticketSaleDTO.getVoyageUID()).size() + ticketSaleDTO.getTicketItems().size()) > 5)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("you have reached ticket limit (5) or man ticket count (2)");

            int maleTicketCount = 0;
            for (TicketItem item : ticketSaleDTO.getTicketItems()) {
                if (item.getGender() == 1) maleTicketCount++;
            }
            if ( maleTicketCount > 2)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("you have reached ticket limit (5) or man ticket count (2)");
        }
        List<Ticket> tickets=new ArrayList<>();
        int ticketPrice = requestMethods.getTicketPrice(ticketSaleDTO.getVoyageUID());

        for(TicketItem ticket : ticketSaleDTO.getTicketItems()){
            ResponseEntity seatStatus = requestMethods
                    .getSeat(ticketSaleDTO.getVoyageUID(), ticket.getSeatNo(), ticket.getGender());
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
