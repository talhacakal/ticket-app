package com.voyage.Controller;

import com.voyage.Annotation.Authorize;
import com.voyage.Annotation.Role;
import com.voyage.Config.RequestMethods;
import com.voyage.Entity.DTO.VoyageDTO;
import com.voyage.Entity.Enum.GenderType;
import com.voyage.Entity.Enum.SeatStatus;
import com.voyage.Entity.Enum.VehicleType;
import com.voyage.Entity.Seat;
import com.voyage.Entity.Voyage;
import com.voyage.Repository.BussCompanyRepository;
import com.voyage.Repository.VoyageRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.WebUtils;

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
    private final RequestMethods requestMethods;

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

        return ResponseEntity.ok(list.stream().map(VoyageDTO::new).toList());
    }
    @GetMapping(params = {"from","to","departureDate"})
    public ResponseEntity<List<VoyageDTO>> searchVoyageByDepartureTime
            (@RequestParam String from, @RequestParam String to, @RequestParam String departureDate) throws ParseException {
        List<Voyage> list = this.voyageRepository
                .findByFromWhereAndToWhereAndDepartureDateOrderByDepartureTimeAsc(from,to,new SimpleDateFormat("yyyy-MM-dd").parse(departureDate));
        if (list.size() == 0) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(list.stream().map(VoyageDTO::new).toList());
    }

    @PostMapping(path = "")
    @Authorize(role = Role.ROLE_ADMIN)
    public ResponseEntity<Object> addVoyage(HttpServletRequest request, @RequestBody VoyageDTO voyageDTO){
        Voyage voyage = new Voyage(
                this.requestMethods.getUUID(WebUtils.getCookie(request, "Authorization").getValue()),
                voyageDTO,
                this.bussCompanyRepository.findByName(voyageDTO.getVehicleCompany()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid Comapny Name")),
                getSeatList(voyageDTO.getVehicleType())
        );
        this.voyageRepository.save(voyage);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PutMapping(path = "")
    @Authorize(role = Role.ROLE_ADMIN)
    public ResponseEntity<Object> updateVoyage(HttpServletRequest request, @RequestBody VoyageDTO voyageDTO){
        Voyage voyage = this.voyageRepository.findByVoyageUUID(voyageDTO.getVoyageUUID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voyage not found. Voyage ID invalid"));

        voyage.updateVoyage(
                voyageDTO,
                voyage.getVehicleCompany().getName().equalsIgnoreCase(voyageDTO.getVehicleCompany())
                ? voyage.getVehicleCompany()
                : this.bussCompanyRepository.findByName(voyageDTO.getVehicleCompany())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voyage not found. Voyage ID invalid")));

        this.voyageRepository.save(voyage);
        return ResponseEntity.ok().build();
    }
    @PutMapping(path = "", params = {"voyageUUID","status"})
    @Authorize(role = Role.ROLE_ADMIN)
    public ResponseEntity<Object> updateVoyageStatus(HttpServletRequest request, @RequestParam String voyageUUID, @RequestParam boolean status){
        var updatedVoyage = this.voyageRepository.findByVoyageUUID(voyageUUID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seats not found. Voyage ID invalid"));
        updatedVoyage.setVoyageStatus(status);
        this.voyageRepository.save(updatedVoyage);

        return ResponseEntity.ok().build();
    }

    List<Seat> getSeatList(String vehicleType) {
        int seats;

        if (vehicleType.equals("Otobüs")) seats = 45;
        else seats = 189;

        List<Seat> seatList = new ArrayList<>();
        for (int i = 1; i <= seats; i++) {
            seatList.add(new Seat(i, SeatStatus.BOS, GenderType.EMPTY));
        }
        return seatList;
    }
}
