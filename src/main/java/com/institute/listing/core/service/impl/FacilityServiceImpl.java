package com.institute.listing.core.service.impl;

import com.institute.listing.core.dto.FacilityRequestDTO;
import com.institute.listing.core.dto.FacilityResponseDTO;
import com.institute.listing.core.exception.NotFoundException;
import com.institute.listing.core.mapper.FacilityMapper;
import com.institute.listing.core.model.Facility;
import com.institute.listing.core.model.Institution;
import com.institute.listing.core.repository.FacilityRepository;
import com.institute.listing.core.repository.InstitutionRepository;
import com.institute.listing.core.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FacilityServiceImpl implements FacilityService {

    private final FacilityRepository facilityRepository;
    private final InstitutionRepository institutionRepository;

    @Override
    public FacilityResponseDTO createFacility(FacilityRequestDTO dto) {
        Institution institution = findInstitution(dto.getInstitutionId());

        Facility facility = FacilityMapper.fromDTO(dto, institution);
        Facility saved = facilityRepository.save(facility);

        return FacilityMapper.toDTO(saved);
    }

    @Override
    public FacilityResponseDTO updateFacility(Long id, FacilityRequestDTO dto) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Facility not found with id " + id));

        Institution institution = findInstitution(dto.getInstitutionId());

        facility.setAvailable(dto.getAvailable());
        facility.setDescription(dto.getDescription());
        facility.setAdditionalInfo(dto.getAdditionalInfo());
        facility.setInstitution(institution);
        facility.setServices(FacilityMapper.serializeServices(dto.getServices()));

        Facility updated = facilityRepository.save(facility);
        return FacilityMapper.toDTO(updated);
    }

    @Override
    public void deleteFacility(Long id) {
        if (!facilityRepository.existsById(id)) {
            throw new NotFoundException("Facility not found with id " + id);
        }
        facilityRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FacilityResponseDTO> getFacilityById(Long id) {
        return facilityRepository.findById(id).map(FacilityMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacilityResponseDTO> getAllFacilities() {
        return facilityRepository.findAll()
                .stream()
                .map(FacilityMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacilityResponseDTO> getFacilitiesByInstitution(Long institutionId) {
        return facilityRepository.findByInstitutionId(institutionId)
                .stream()
                .map(FacilityMapper::toDTO)
                .toList();
    }

    /**
     * Helper method to fetch institution or throw exception if not found
     */
    private Institution findInstitution(Long id) {
        return institutionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Institution not found with id " + id));
    }
}
