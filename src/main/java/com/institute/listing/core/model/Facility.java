package com.institute.listing.core.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "facilities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "facilities_id_seq")
    @SequenceGenerator(name = "facilities_id_seq", sequenceName = "facilities_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id", nullable = false)
    private Institution institution;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean available;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Facility)) return false;
        return id != null && id.equals(((Facility) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
