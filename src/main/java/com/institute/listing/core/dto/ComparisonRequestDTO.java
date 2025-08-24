package com.institute.listing.core.dto;

import lombok.Data;
import java.util.List;

@Data
public class ComparisonRequestDTO {
    private List<Long> institutionIds;
    private String requesterKey;
}
