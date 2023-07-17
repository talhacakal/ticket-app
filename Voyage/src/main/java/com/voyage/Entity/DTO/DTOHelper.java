package com.voyage.Entity.DTO;

import com.voyage.Entity.Voyage;

import java.util.List;

public class DTOHelper {

    /* Voyage */
    public static VoyageDTO getDto(Voyage voyage) {
        return new VoyageDTO(
                voyage.getVoyageUUID(),
                voyage.getVehicleCompany().getName(),
                voyage.getVehicleType().getVehicleType(),
                voyage.getBussType().getBussType(),
                voyage.getFromWhere(),
                voyage.getToWhere(),
                voyage.getDepartureTime(),
                voyage.getDepartureDate(),
                voyage.getTravelDuration(),
                voyage.getPrice(),
                voyage.getSeats().size(),
                voyage.isVoyageStatus()
        );
    }
    public static List<VoyageDTO> getDtos(List<Voyage> voyages) {
        return voyages.stream().map(DTOHelper::getDto).toList();
    }

    /* Seat */
    public static List<SeatDTO> getSeats(Voyage voyage){
        return voyage.getSeats().stream()
                .map(item -> new SeatDTO(
                        item.getNumber(),
                        item.getSeatStatus().getSeatStatus(),
                        item.getGenderType().getGenderValue())).toList();
    }

}
