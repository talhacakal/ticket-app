package com.voyage.Controller;

import com.voyage.DTO.DTOHelper;
import com.voyage.DTO.SeatDTO;
import com.voyage.Entity.Enum.GenderType;
import com.voyage.Entity.Enum.SeatStatus;
import com.voyage.Entity.Seat;
import com.voyage.Repository.SeatRepository;
import com.voyage.Repository.VoyageRepository;
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
                DTOHelper.getSeats(this.voyageRepository.findByVoyageUUID(voyageUUID)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seats not found. Voyaga ID invalid"))));
    }
    @PutMapping(value = "/{voyageUUID}")
    public ResponseEntity updateSeats(@PathVariable String voyageUUID, @RequestBody List<SeatDTO> seatDTOList){
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
