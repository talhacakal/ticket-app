package com.voyage.Repository;

import com.voyage.Entity.BussCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BussCompanyRepository extends JpaRepository<BussCompany, Long> {
    Optional<BussCompany> findByName(String name);

}