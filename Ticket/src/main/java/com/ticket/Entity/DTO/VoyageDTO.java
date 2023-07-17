package com.ticket.Entity.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;

@Data
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
    private boolean voyageStatus;

}
