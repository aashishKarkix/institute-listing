package com.institute.listing.core.model;

import com.institute.listing.core.dto.InstitutionDTO;
import com.institute.listing.core.mapper.FacilityMapper;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "institutions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "institutions_id_seq")
    @SequenceGenerator(name = "institutions_id_seq", sequenceName = "institutions_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String type; // school/college/university
    private String location;

    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Facility> facilities = new HashSet<>();

    @Column(name = "avg_rating")
    private Double avgRating;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Institution)) return false;
        return id != null && id.equals(((Institution) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public InstitutionDTO toDTO() {
        return InstitutionDTO.builder()
                .id(this.id)
                .name(this.name)
                .type(this.type)
                .location(this.location)
                .avgRating(this.avgRating)
                .reviews(this.reviews != null
                        ? this.reviews.stream().map(Review::toDTO).toList()
                        : List.of())
                .facilities(this.facilities != null
                        ? this.facilities.stream().map(FacilityMapper::toDTO).toList()
                        : List.of())
                .build();
    }

    public static Institution fromDTO(InstitutionDTO dto) {
        return Institution.builder()
                .id(dto.getId())
                .name(dto.getName())
                .type(dto.getType())
                .location(dto.getLocation())
                .avgRating(dto.getAvgRating())
                .build();
    }
}
