package com.voyage.Repository;

import com.voyage.Entity.VehicleCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BussCompanyRepository extends JpaRepository<VehicleCompany, Long> {
    Optional<VehicleCompany> findByName(String name);

}