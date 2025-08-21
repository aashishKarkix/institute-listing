package com.institute.listing.core.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityServiceDTO {

    @NotBlank(message = "Service name is required")
    private String name;
    private String info;
}
