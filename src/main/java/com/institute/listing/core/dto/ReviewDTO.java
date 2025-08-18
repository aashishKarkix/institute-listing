package com.institute.listing.core.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
    private Long id;
    private Long institutionId;
    private Double rating;
    private String comment;
    private LocalDateTime createdAt;
}
