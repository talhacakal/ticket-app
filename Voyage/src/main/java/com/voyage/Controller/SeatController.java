package com.voyage.Controller;

import com.voyage.Annotation.Authorize;
import com.voyage.Annotation.Role;
import com.voyage.Entity.DTO.SeatDTO;
import com.voyage.Entity.Enum.GenderType;
import com.voyage.Entity.Enum.SeatStatus;
import com.voyage.Entity.Seat;
import com.voyage.Repository.SeatRepository;
import com.voyage.Repository.VoyageRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/seat")
@RequiredArgsConstructor
public class SeatController {

    private final SeatRepository seatRepository;
    private final VoyageRepository voyageRepository;

    @GetMapping("/{voyageUUID}")
    public ResponseEntity<List<SeatDTO>> getSeats(@PathVariable String voyageUUID){
        return ResponseEntity.ok(
                this.voyageRepository.findByVoyageUUID(voyageUUID)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seats not found. Voyaga ID invalid"))
                        .getSeats().stream().map(SeatDTO::new).toList()
        );
    }
    @PostMapping(params = {"gender","voyageUID","seatNo"})
    public ResponseEntity ticketSale(@RequestParam String voyageUID, @RequestParam int seatNo, @RequestParam int gender){
        Seat seat = this.seatRepository.findByVoyage_VoyageUUIDAndNumber(voyageUID, seatNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seats not found. Voyaga ID or seat number invalid"));

        if (seat.getSeatStatus() == SeatStatus.DOLU) return ResponseEntity.status(HttpStatus.CONFLICT).body("Seat is not empty.");

        seat.setSeatStatus(SeatStatus.DOLU);
        seat.setGenderType(GenderType.fromInt(gender));
        this.seatRepository.save(seat);
        return ResponseEntity.ok().build();
    }
    @PutMapping(value = "/{voyageUUID}")
    @Authorize(role = Role.ROLE_ADMIN)
    public ResponseEntity updateSeats(HttpServletRequest request, @PathVariable String voyageUUID, @RequestBody List<SeatDTO> seatDTOList){
        seatDTOList.forEach(item -> {
            var seat = this.seatRepository.findByVoyage_VoyageUUIDAndNumber(voyageUUID, item.getNumber())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seats not found. Voyaga ID or seat number invalid"));
            seat.setSeatStatus(SeatStatus.fromInt(item.getStatus()));
            seat.setGenderType(GenderType.fromInt(item.getGender()));
            this.seatRepository.save(seat);
        });
        return ResponseEntity.ok().build();
    }

}
