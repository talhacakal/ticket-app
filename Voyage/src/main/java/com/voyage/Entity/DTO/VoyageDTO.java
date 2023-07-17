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
    private String bussType;
    private String from;
    private String to;
    private Time departureTime;
    private Date departureDate;
    private int travelDuration;
    private int seats;
    private boolean voyageStatus;
    private int price;

    public VoyageDTO(String voyageUUID, String vehicleCompany, String vehicleType, String bussType, String from, String to, Time departureTime, Date departureDate, int travelDuration, int price, int seats, boolean voyageStatus) {
        this.voyageUUID = voyageUUID;
        this.vehicleCompany = vehicleCompany;
        this.vehicleType = vehicleType;
        this.bussType = bussType;
        this.from = from;
        this.to = to;
        this.departureTime = departureTime;
        this.departureDate = departureDate;
        this.travelDuration = travelDuration;
        this.price = price;
        this.voyageStatus = voyageStatus;
        this.seats = seats;
    }

    public VoyageDTO(Voyage voyage) {
        this.voyageUUID = voyage.getVoyageUUID();
        this.vehicleCompany = voyage.getVehicleCompany().getName();
        this.vehicleType = voyage.getVehicleType().getVehicleType();
        this.bussType = voyage.getBussType().getBussType();
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
