package com.institute.listing.core.service.impl;

import com.institute.listing.core.dto.InstitutionDTO;
import com.institute.listing.core.exception.NotFoundException;
import com.institute.listing.core.model.Institution;
import com.institute.listing.core.repository.InstitutionRepository;
import com.institute.listing.core.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InstitutionServiceImpl implements InstitutionService {

    private final InstitutionRepository repository;

    @Override
    public InstitutionDTO createInstitution(InstitutionDTO dto) {
        Institution saved = repository.save(Institution.fromDTO(dto));
        return saved.toDTO();
    }

    @Override
    public InstitutionDTO updateInstitution(Long id, InstitutionDTO dto) {
        Institution existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Institution not found"));

        existing.setName(dto.getName());
        existing.setType(dto.getType());
        existing.setLocation(dto.getLocation());
        // avgRating is updated via reviews

        return repository.save(existing).toDTO();
    }

    @Override
    public void deleteInstitution(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Institution not found");
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public InstitutionDTO getInstitution(Long id) {
        return repository.findById(id)
                .map(Institution::toDTO)
                .orElseThrow(() -> new NotFoundException("Institution not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstitutionDTO> getAllInstitutions() {
        return repository.findAll().stream()
                .map(Institution::toDTO)
                .toList();
    }
}
