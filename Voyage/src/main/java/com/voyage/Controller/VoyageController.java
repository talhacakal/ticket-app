package com.voyage.Controller;

import com.voyage.Entity.DTO.DTOHelper;
import com.voyage.Entity.DTO.VoyageDTO;
import com.voyage.Entity.Enum.BussType;
import com.voyage.Entity.Enum.GenderType;
import com.voyage.Entity.Enum.SeatStatus;
import com.voyage.Entity.Enum.VehicleType;
import com.voyage.Entity.Seat;
import com.voyage.Entity.Voyage;
import com.voyage.Repository.BussCompanyRepository;
import com.voyage.Repository.VoyageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/voyage")
@RequiredArgsConstructor
public class VoyageController {

    private final VoyageRepository voyageRepository;
    private final BussCompanyRepository bussCompanyRepository;
    @GetMapping(path = "/{voyageUUID}")
    public ResponseEntity<VoyageDTO> getVoyage(@PathVariable String voyageUUID){
        return ResponseEntity.ok(
                this.voyageRepository.findByVoyageUUID(voyageUUID)
                        .map(VoyageDTO::new)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seats not found. Voyaga ID invalid"))
        );
    }
    @GetMapping(params = {"voyageUUID"})
    public ResponseEntity<Integer> getVoyagePrice(@RequestParam String voyageUUID){
        return ResponseEntity.ok(
                this.voyageRepository.getVoyagePrice(voyageUUID)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "voyageUUID not found. Voyaga ID invalid"))
        );
    }
    @GetMapping(params = {"from","to","vehicleType"})
    public ResponseEntity<List<VoyageDTO>> searchVoyageByVehicle(@RequestParam String from, @RequestParam String to, @RequestParam String vehicleType) {
        List<Voyage> list = this.voyageRepository.findByFromWhereAndToWhereAndVehicleTypeOrderByDepartureTimeAsc(from, to, VehicleType.fromString(vehicleType));
        if (list.size() == 0) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(DTOHelper.getDtos(list));
    }
    @GetMapping(params = {"from","to","departureDate"})
    public ResponseEntity<List<VoyageDTO>> searchVoyageByDepartureTime(@RequestParam String from, @RequestParam String to, @RequestParam String departureDate) throws ParseException {
        List<Voyage> list = this.voyageRepository
                .findByFromWhereAndToWhereAndDepartureDateOrderByDepartureTimeAsc(from,to,new SimpleDateFormat("yyyy-MM-dd").parse(departureDate));
        if (list.size() == 0) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(DTOHelper.getDtos(list));
    }

    /* Ony ADMIN  */
    // Add New Voyage
    @PostMapping(path = "")    //TODO: Only Admin
    public ResponseEntity<Object> addVoyage(@RequestBody VoyageDTO voyageDTO){
        Voyage voyage = Voyage.builder()
                .UUID("36dec986-5070-4c72-b031-223dc174756e") //TODO: update here
                .departureDate(voyageDTO.getDepartureDate())
                .departureTime(voyageDTO.getDepartureTime())
                .travelDuration(voyageDTO.getTravelDuration())
                .vehicleType(VehicleType.fromString(voyageDTO.getVehicleType()))
                .bussType(BussType.fromString(voyageDTO.getBussType()))
                .fromWhere(voyageDTO.getFrom())
                .toWhere(voyageDTO.getTo())
                .price(voyageDTO.getPrice())
                .vehicleCompany(this.bussCompanyRepository.findByName(voyageDTO.getVehicleCompany())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid Comapny Name")))
                .seats(getSeatList(voyageDTO.getSeats()))
                .voyageStatus(true)
                .build();
        this.voyageRepository.save(voyage);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Update Voyage
    @PutMapping(path = "")  //TODO: Only Admin
    public ResponseEntity<Object> updateVoyage(@RequestBody VoyageDTO voyageDTO){
        var voyage = this.voyageRepository.findByVoyageUUID(voyageDTO.getVoyageUUID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voyage not found. Voyage ID invalid"));

        voyage.setDepartureDate(voyageDTO.getDepartureDate());
        voyage.setDepartureTime(voyageDTO.getDepartureTime());;
        voyage.setTravelDuration(voyageDTO.getTravelDuration());
        voyage.setVehicleType(VehicleType.fromString(voyageDTO.getVehicleType()));
        voyage.setBussType(BussType.fromString(voyageDTO.getBussType()));
        voyage.setFromWhere(voyage.getFromWhere());
        voyage.setToWhere(voyage.getToWhere());
        voyage.setPrice(voyageDTO.getPrice());
        voyage.setVoyageStatus(voyage.isVoyageStatus());
        voyage.setVehicleCompany(voyage.getVehicleCompany().getName().equalsIgnoreCase(voyageDTO.getVehicleCompany())
                ? voyage.getVehicleCompany()
                : this.bussCompanyRepository.findByName(voyageDTO.getVehicleCompany())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voyage not found. Voyage ID invalid")));

        this.voyageRepository.save(voyage);

        return ResponseEntity.ok().build();
    }

    // Update Voyage Status
    @PutMapping(path = "", params = {"voyageUUID","status"}) //TODO: Only Admin
    public ResponseEntity<Object> updateVoyageStatus(@RequestParam String voyageUUID, @RequestParam boolean status){
        var updatedVoyage = this.voyageRepository.findByVoyageUUID(voyageUUID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seats not found. Voyage ID invalid"));
        updatedVoyage.setVoyageStatus(status);
        this.voyageRepository.save(updatedVoyage);

        return ResponseEntity.ok().build();
    }

    /* Other Metots */
    List<Seat> getSeatList(int seats){
        List<Seat> seatList = new ArrayList<>();
        for (int i = 1; i <= seats; i++) {
            seatList.add(new Seat(i, SeatStatus.BOS, GenderType.EMPTY));
        }
        return seatList;
    }
}
