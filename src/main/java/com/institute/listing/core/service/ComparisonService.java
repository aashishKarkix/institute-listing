package com.institute.listing.core.service;

import com.institute.listing.core.dto.ComparisonRequestDTO;
import com.institute.listing.core.dto.ComparisonResponseDTO;
import org.springframework.security.core.Authentication;

public interface ComparisonService {
    ComparisonResponseDTO compareInstitutions(ComparisonRequestDTO request, Authentication authentication);
}
