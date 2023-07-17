package com.voyage.Repository;

import com.voyage.Entity.Enum.VehicleType;
import com.voyage.Entity.Voyage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface VoyageRepository extends JpaRepository<Voyage, Long> {
    List<Voyage> findByFromWhereAndToWhereAndVehicleTypeOrderByDepartureTimeAsc(String fromWhere, String toWhere, VehicleType vehicleType);
    List<Voyage> findByFromWhereAndToWhereAndDepartureDateOrderByDepartureTimeAsc(String fromWhere, String toWhere, Date departureDate);
    @Query("select v.price from Voyage v where v.voyageUUID = ?1")
    Optional<Integer> getVoyagePrice(String voyageUUID);
    Optional<Voyage> findByVoyageUUID(String voyageUUID);

}