package com.institute.listing.core.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.institute.listing.core.dto.*;
import com.institute.listing.core.model.Facility;
import com.institute.listing.core.model.Institution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FacilityMapper {

    private FacilityMapper() {
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Converts a Facility entity to a FacilityResponseDTO.
     *
     * @param facility the Facility entity to convert
     * @return FacilityResponseDTO with all relevant fields populated
     */
    public static FacilityResponseDTO toDTO(Facility facility) {
        return FacilityResponseDTO.builder()
                .id(facility.getId())
                .institutionId(facility.getInstitution() != null ? facility.getInstitution().getId() : null)
                .available(facility.getAvailable())
                .services(parseServices(facility.getServices()))
                .description(facility.getDescription())
                .additionalInfo(facility.getAdditionalInfo())
                .createdAt(facility.getCreatedAt())
                .updatedAt(facility.getUpdatedAt())
                .build();
    }

    /**
     * Converts a FacilityRequestDTO and Institution entity to a Facility entity.
     *
     * @param dto         the DTO containing request data
     */
    public static Facility fromDTO(FacilityRequestDTO dto, Institution institution) {
        return Facility.builder()
                .available(dto.getAvailable())
                .description(dto.getDescription())
                .additionalInfo(dto.getAdditionalInfo())
                .institution(institution)
                .services(serializeServices(dto.getServices()))
                .build();
    }

    /**
     * Parses a JSON string representing a list of FacilityServiceDTO objects.
     *
     * @param json the JSON string to parse
     */
    public static List<FacilityServiceDTO> parseServices(String json) {
        if (json == null || json.isEmpty()) return new ArrayList<>();
        try {
            return Arrays.asList(objectMapper.readValue(json, FacilityServiceDTO[].class));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting JSON to ServiceDTO list", e);
        }
    }

    /**
     * Serializes a list of FacilityServiceDTO objects to a JSON string.
     *
     * @param services the list of services to serialize
     */
    public static String serializeServices(List<FacilityServiceDTO> services) {
        if (services == null) return null;
        try {
            return objectMapper.writeValueAsString(services);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting ServiceDTO list to JSON", e);
        }
    }
}
