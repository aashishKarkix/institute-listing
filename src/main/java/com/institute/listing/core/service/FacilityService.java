package com.institute.listing.core.service;

import com.institute.listing.core.dto.FacilityRequestDTO;
import com.institute.listing.core.dto.FacilityResponseDTO;

import java.util.List;
import java.util.Optional;

public interface FacilityService {

    FacilityResponseDTO createFacility(FacilityRequestDTO dto);

    FacilityResponseDTO updateFacility(Long id, FacilityRequestDTO dto);

    void deleteFacility(Long id);

    Optional<FacilityResponseDTO> getFacilityById(Long id);

    List<FacilityResponseDTO> getAllFacilities();

    List<FacilityResponseDTO> getFacilitiesByInstitution(Long institutionId);
}
