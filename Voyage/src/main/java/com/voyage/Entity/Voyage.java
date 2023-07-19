package com.voyage.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voyage.Entity.DTO.VoyageDTO;
import com.voyage.Entity.Enum.VehicleType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "voyage")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Voyage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_user_uuid", nullable = false)
    private String UUID;

    @Column(name = "voyage_uuid", nullable = false, unique = true)
    private String voyageUUID;

    @Column(name = "departure_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date departureDate;

    @Column(name = "departure_time", nullable = false)
    private Time departureTime;

    @Column(name = "travel_duration", nullable = false)
    private int travelDuration;

    @Column(name = "vehicle_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    @Column(name = "from_where", nullable = false)
    private String fromWhere;

    @Column(name = "to_where", nullable = false)
    private String toWhere;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "status", nullable = false)
    private boolean voyageStatus;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "vehicle_company_id")
    private VehicleCompany vehicleCompany;

    @OneToMany(fetch = FetchType.LAZY   ,cascade = CascadeType.ALL)
    @JoinColumn(name = "voyage_id", referencedColumnName = "id")
    @JsonIgnore
    private List<Seat> seats;

    @Column(name = "create_date", nullable = false)
    @CreationTimestamp
    private LocalDateTime createDate;

    @Column(name = "last_update_date", nullable = false, columnDefinition = "true")
    @UpdateTimestamp
    private LocalDateTime lastUpdateDate;

    @PrePersist
    public void prePersist() {
        this.voyageUUID = java.util.UUID.randomUUID().toString();
    }

    public void updateVoyage(VoyageDTO voyageDTO, VehicleCompany vehicleCompany){
        this.departureDate = voyageDTO.getDepartureDate();
        this.departureTime = voyageDTO.getDepartureTime();
        this.travelDuration = voyageDTO.getTravelDuration();
        this.vehicleType = VehicleType.fromString(voyageDTO.getVehicleType());
        this.fromWhere = voyageDTO.getFrom();
        this.toWhere = voyageDTO.getTo();
        this.price = voyageDTO.getPrice();
        this.voyageStatus = voyageDTO.isVoyageStatus();
        this.vehicleCompany = vehicleCompany;
    }

    public Voyage(String UUID, VoyageDTO voyageDTO, VehicleCompany vehicleCompany, List<Seat> seats){
        this.UUID = UUID;
        this.voyageUUID = voyageDTO.getVoyageUUID();
        this.departureDate = voyageDTO.getDepartureDate();
        this.departureTime = voyageDTO.getDepartureTime();
        this.travelDuration = voyageDTO.getTravelDuration();
        this.vehicleType = VehicleType.fromString(voyageDTO.getVehicleType());
        this.fromWhere = voyageDTO.getFrom();
        this.toWhere = voyageDTO.getTo();
        this.price = voyageDTO.getPrice();
        this.voyageStatus = voyageDTO.isVoyageStatus();
        this.vehicleCompany = vehicleCompany;
        this.seats = seats;
    }

}
