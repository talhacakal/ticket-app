package com.ticket.Repository;

import com.ticket.Entity.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TicketRepository extends MongoRepository<Ticket, String> {
    List<Ticket> findByUUID(String UUID);
    List<Ticket> findByVoyageUID(String voyageUID);
    List<Ticket> findByUUIDAndVoyageUID(String UUID, String voyageUID);

}
