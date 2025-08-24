package com.institute.listing.core.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private Long reviewId;
    private Long institutionId;
    private Long userId;
    private String userName;
    private String commentText;
    private LocalDateTime createdAt;
}
