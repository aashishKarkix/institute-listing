package com.institute.listing.core.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ComparisonResponseDTO {
    private List<InstitutionDTO> institutions;
    private String message;
}
