package com.institute.listing.core.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
}
