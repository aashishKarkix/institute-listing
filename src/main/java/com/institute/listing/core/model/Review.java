package com.institute.listing.core.model;

import com.institute.listing.core.dto.ReviewDTO;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "reviews_id_seq")
    @SequenceGenerator(name = "reviews_id_seq", sequenceName = "reviews_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id")
    private Institution institution;

    private Double rating;
    private String comment;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Review)) return false;
        return id != null && id.equals(((Review) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public ReviewDTO toDTO() {
        return ReviewDTO.builder()
                .id(this.id)
                .institutionId(this.institution != null ? this.institution.getId() : null)
                .rating(this.rating)
                .comment(this.comment)
                .createdAt(this.createdAt)
                .build();
    }

    public static Review fromDTO(ReviewDTO dto, User user, Institution institution) {
        return Review.builder()
                .id(dto.getId())
                .user(user)
                .institution(institution)
                .rating(dto.getRating())
                .comment(dto.getComment())
                .build();
    }
}
