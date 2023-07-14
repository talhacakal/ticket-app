package com.voyage.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voyage.Entity.Enum.BussType;
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

    @Column(name = "buss_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private BussType bussType;

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
    private BussCompany vehicleCompany;

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

    public Voyage(String UUID, String voyageUUID, Time departureTime, int travelDuration, BussType bussType, String fromWhere, String toWhere, VehicleType vehicleType, Date departureDate, int price, BussCompany vehicleCompany) {
        this.UUID = UUID;
        this.voyageUUID = voyageUUID;
        this.departureTime = departureTime;
        this.travelDuration = travelDuration;
        this.bussType = bussType;
        this.fromWhere = fromWhere;
        this.toWhere = toWhere;
        this.vehicleType = vehicleType;
        this.departureDate = departureDate;
        this.price = price;
        this.vehicleCompany = vehicleCompany;
    }
}
