package com.institute.listing.core.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
