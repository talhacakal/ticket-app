package com.voyage.Repository;

import com.voyage.Entity.Enum.VehicleType;
import com.voyage.Entity.Voyage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface VoyageRepository extends JpaRepository<Voyage, Long> {
    List<Voyage> findByFromWhereAndToWhereAndVehicleTypeOrderByDepartureTimeAsc(String fromWhere, String toWhere, VehicleType vehicleType);
    List<Voyage> findByFromWhereAndToWhereAndDepartureDateOrderByDepartureTimeAsc(String fromWhere, String toWhere, Date departureDate);
    Optional<Voyage> findByVoyageUUID(String voyageUUID);

}