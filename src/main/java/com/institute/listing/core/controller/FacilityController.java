package com.institute.listing.core.controller;

import com.institute.listing.core.dto.FacilityRequestDTO;
import com.institute.listing.core.dto.FacilityResponseDTO;
import com.institute.listing.core.security.annotation.Admin;
import com.institute.listing.core.security.annotation.User;
import com.institute.listing.core.service.FacilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/facilities")
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityService facilityService;

    @Admin
    @PostMapping
    public ResponseEntity<FacilityResponseDTO> createFacility(@Valid @RequestBody FacilityRequestDTO requestDTO) {
        return ResponseEntity.ok(facilityService.createFacility(requestDTO));
    }

    @Admin
    @PutMapping("/{id}")
    public ResponseEntity<FacilityResponseDTO> updateFacility(@PathVariable Long id,
                                                              @Valid @RequestBody FacilityRequestDTO requestDTO) {
        return ResponseEntity.ok(facilityService.updateFacility(id, requestDTO));
    }

    @Admin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacility(@PathVariable Long id) {
        facilityService.deleteFacility(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<FacilityResponseDTO>> getFacilityById(@PathVariable Long id) {
        return ResponseEntity.ok(facilityService.getFacilityById(id));
    }

    @GetMapping
    public ResponseEntity<List<FacilityResponseDTO>> getAllFacilities() {
        return ResponseEntity.ok(facilityService.getAllFacilities());
    }

    @GetMapping("/institution/{institutionId}")
    public ResponseEntity<List<FacilityResponseDTO>> getFacilitiesByInstitution(@PathVariable Long institutionId) {
        return ResponseEntity.ok(facilityService.getFacilitiesByInstitution(institutionId));
    }
}
