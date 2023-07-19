package com.voyage.Entity.DTO;

import com.voyage.Entity.Voyage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;

@Data
@NoArgsConstructor
public class VoyageDTO {

    private String voyageUUID;
    private String vehicleCompany;
    private String vehicleType;
    private String from;
    private String to;
    private Time departureTime;
    private Date departureDate;
    private int travelDuration;
    private int seats;
    private boolean voyageStatus;
    private int price;

    public VoyageDTO(Voyage voyage) {
        this.voyageUUID = voyage.getVoyageUUID();
        this.vehicleCompany = voyage.getVehicleCompany().getName();
        this.vehicleType = voyage.getVehicleType().getVehicleType();
        this.from = voyage.getFromWhere();
        this.to = voyage.getToWhere();
        this.departureTime = voyage.getDepartureTime();
        this.departureDate = voyage.getDepartureDate();
        this.travelDuration = voyage.getTravelDuration();
        this.price = voyage.getPrice();
        this.voyageStatus = voyage.isVoyageStatus();
        this.seats = voyage.getSeats().size();
    }
}
