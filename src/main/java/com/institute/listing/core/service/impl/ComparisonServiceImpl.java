package com.institute.listing.core.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.institute.listing.core.dto.ComparisonRequestDTO;
import com.institute.listing.core.dto.ComparisonResponseDTO;
import com.institute.listing.core.dto.InstitutionDTO;
import com.institute.listing.core.exception.NotFoundException;
import com.institute.listing.core.model.Comparison;
import com.institute.listing.core.model.Institution;
import com.institute.listing.core.model.User;
import com.institute.listing.core.repository.ComparisonRepository;
import com.institute.listing.core.repository.InstitutionRepository;
import com.institute.listing.core.service.ComparisonService;
import com.institute.listing.core.util.AuthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ComparisonServiceImpl implements ComparisonService {

    private static final Logger log = LoggerFactory.getLogger(ComparisonServiceImpl.class);

    private static final String USER_PREFIX = "user:";
    private static final String ANON_PREFIX = "anon:";
    private static final String MSG_COMPARISON_SUCCESS = "Comparison successful";
    private static final String MSG_LIMIT_REACHED = "You have reached max %d comparisons. Please login to continue.";

    @Value("${max.count.before.login:5}")
    private Integer maxCount;

    private final ComparisonRepository comparisonRepository;
    private final InstitutionRepository institutionRepository;
    private final AuthUtil authUtil;
    private final Cache<String, Integer> comparisonCache;

    public ComparisonServiceImpl(
            ComparisonRepository comparisonRepository,
            InstitutionRepository institutionRepository,
            AuthUtil authUtil,
            @Qualifier("comparisonCache") Cache<String, Integer> comparisonCache
    ) {
        this.comparisonRepository = comparisonRepository;
        this.institutionRepository = institutionRepository;
        this.authUtil = authUtil;
        this.comparisonCache = comparisonCache;
    }

    @Override
    public ComparisonResponseDTO compareInstitutions(ComparisonRequestDTO request, OAuth2User oauthUser) {
        User authenticatedUser = (oauthUser != null) ? authUtil.getAuthenticatedUser(oauthUser) : null;
        String requesterKey = buildRequesterKey(authenticatedUser, request.getRequesterKey());

        enforceAnonymousLimitIfNeeded(authenticatedUser, requesterKey);

        List<Institution> institutions = fetchInstitutions(request.getInstitutionIds());

        saveComparison(authenticatedUser, requesterKey, institutions);

        List<InstitutionDTO> institutionDTOs = mapToDTOs(institutions);

        log.info("Comparison successful for requesterKey={}", requesterKey);
        return buildResponse(institutionDTOs);
    }

    private String buildRequesterKey(User authenticatedUser, String requesterKey) {
        if (authenticatedUser != null) {
            return USER_PREFIX + authenticatedUser.getId();
        } else if (requesterKey != null && !requesterKey.isBlank()) {
            return ANON_PREFIX + requesterKey;
        } else {
            return ANON_PREFIX + "unknown";
        }
    }

    private void enforceAnonymousLimitIfNeeded(User authenticatedUser, String requesterKey) {
        if (authenticatedUser == null) {
            comparisonCache.asMap().merge(requesterKey, 1, Integer::sum);
            Integer count = comparisonCache.getIfPresent(requesterKey);
            if (count != null && count > maxCount) {
                log.warn("Anonymous user reached limit: requesterKey={}, count={}", requesterKey, count);
                throw new AccessDeniedException(String.format(MSG_LIMIT_REACHED, maxCount));
            }
        }
    }

    private List<Institution> fetchInstitutions(List<Long> institutionIds) {
        List<Institution> institutions = institutionRepository.findAllById(institutionIds);
        if (institutions.size() != institutionIds.size()) {
            log.warn("Some institutions not found: requestedIds={}", institutionIds);
            throw new NotFoundException("One or more institutions not found");
        }
        return institutions;
    }

    private void saveComparison(User authenticatedUser, String requesterKey, List<Institution> institutions) {
        Comparison comparison = new Comparison();
        comparison.setRequesterKey(requesterKey);
        comparison.setInstitutions(Set.copyOf(institutions));
        if (authenticatedUser != null) {
            comparison.setUser(authenticatedUser);
        }
        comparisonRepository.save(comparison);
        log.debug("Saved comparison for requesterKey={}", requesterKey);
    }

    private List<InstitutionDTO> mapToDTOs(List<Institution> institutions) {
        return institutions.stream()
                .map(Institution::toDTO)
                .toList();
    }

    private ComparisonResponseDTO buildResponse(List<InstitutionDTO> institutionDTOs) {
        return ComparisonResponseDTO.builder()
                .institutions(institutionDTOs)
                .message(MSG_COMPARISON_SUCCESS)
                .build();
    }
}
