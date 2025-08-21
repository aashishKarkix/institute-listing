package com.institute.listing.core.repository;

import com.institute.listing.core.model.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    List<Facility> findByInstitutionId(Long institutionId);
}
