package com.institute.listing.core.service;

import com.institute.listing.core.dto.ComparisonRequestDTO;
import com.institute.listing.core.dto.ComparisonResponseDTO;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface ComparisonService {
    ComparisonResponseDTO compareInstitutions(ComparisonRequestDTO request, OAuth2User oauthUser);
}
