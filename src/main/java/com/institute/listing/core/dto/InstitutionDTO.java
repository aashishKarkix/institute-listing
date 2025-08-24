package com.institute.listing.core.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;
    private String type;
    private String location;
    private Double avgRating;
    private List<ReviewDTO> reviews;
    private List<FacilityResponseDTO> facilities;
}
