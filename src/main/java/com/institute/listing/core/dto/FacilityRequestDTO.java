package com.institute.listing.core.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class FacilityRequestDTO {

    @NotNull(message = "Institution ID is required")
    private Long institutionId;

    @NotNull(message = "Availability must be specified")
    private Boolean available;

    /**
     * Type of service offered by the institution.
     * Examples: Vehicle Service, Canteen, ECA, Fee
     */
    @NotEmpty(message = "Services are required")
    @Valid
    private List<FacilityServiceDTO> services;

    private String description;

    /**
     * optional info like fee amount, timings, etc.
     * */
    private String additionalInfo;
}
