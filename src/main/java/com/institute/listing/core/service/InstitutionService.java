package com.institute.listing.core.service;

import com.institute.listing.core.dto.InstitutionDTO;

import java.util.List;

public interface InstitutionService {
    InstitutionDTO createInstitution(InstitutionDTO dto);

    InstitutionDTO updateInstitution(Long id, InstitutionDTO dto);

    void deleteInstitution(Long id);

    InstitutionDTO getInstitution(Long id);

    List<InstitutionDTO> getAllInstitutions();
}
