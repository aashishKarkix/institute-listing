package com.institute.listing.core.controller;

import com.institute.listing.core.dto.ComparisonRequestDTO;
import com.institute.listing.core.dto.ComparisonResponseDTO;
import com.institute.listing.core.service.ComparisonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comparisons")
@RequiredArgsConstructor
public class ComparisonController {

    private final ComparisonService comparisonService;

    @PostMapping
    public ComparisonResponseDTO compareInstitutions(
            @RequestBody ComparisonRequestDTO request,
            Authentication authentication
    ) {
        return comparisonService.compareInstitutions(request, authentication);
    }
}
