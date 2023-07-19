package com.voyage.Entity.DTO;

import com.voyage.Entity.Seat;
import com.voyage.Entity.Voyage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatDTO {
    private int number;
    private int status;
    private int gender;

    public SeatDTO(Seat seat){
        this.number = seat.getNumber();
        this.status = seat.getSeatStatus().getSeatStatus();
        this.gender = seat.getGenderType().getGenderValue();
    }
}