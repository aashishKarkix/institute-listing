package com.institute.listing.core.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacilityResponseDTO {
    private Long id;
    private Long institutionId;
    private Boolean available;
    private List<FacilityServiceDTO> services;
    private String description;
    private String additionalInfo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
