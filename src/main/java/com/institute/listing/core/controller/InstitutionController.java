package com.institute.listing.core.controller;

import com.institute.listing.core.dto.InstitutionDTO;
import com.institute.listing.core.security.annotation.Admin;
import com.institute.listing.core.service.InstitutionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/institutions")
public class InstitutionController {

    private final InstitutionService service;

    public InstitutionController(InstitutionService service) {
        this.service = service;
    }

    @Admin
    @PostMapping
    public ResponseEntity<InstitutionDTO> create(@RequestBody InstitutionDTO dto) {
        return ResponseEntity.ok(service.createInstitution(dto));
    }

    @Admin
    @PutMapping("/{id}")
    public ResponseEntity<InstitutionDTO> update(@PathVariable Long id, @RequestBody InstitutionDTO dto) {
        return ResponseEntity.ok(service.updateInstitution(id, dto));
    }

    @Admin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteInstitution(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstitutionDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getInstitution(id));
    }

    @GetMapping
    public ResponseEntity<List<InstitutionDTO>> getAll() {
        return ResponseEntity.ok(service.getAllInstitutions());
    }
}
