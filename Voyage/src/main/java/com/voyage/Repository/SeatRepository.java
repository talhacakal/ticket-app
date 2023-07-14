package com.voyage.Repository;

import com.voyage.Entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    Optional<Seat> findByVoyage_VoyageUUIDAndNumber(String voyageUUID, int number);

}