package com.voyage.Entity;

import com.voyage.Entity.Enum.GenderType;
import com.voyage.Entity.Enum.SeatStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seat")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number", nullable = false)
    private int number;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private SeatStatus seatStatus;

    @Column(name = "gender",nullable = false, columnDefinition = "int default 0")
    @Enumerated(EnumType.ORDINAL)
    private GenderType genderType;

    @ManyToOne
    private Voyage voyage;

    public Seat(int number, SeatStatus seatStatus, GenderType genderType) {
        this.number = number;
        this.seatStatus = seatStatus;
        this.genderType = genderType;
    }
}
