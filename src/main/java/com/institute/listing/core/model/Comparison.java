package com.institute.listing.core.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "comparison")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Comparison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "comparison_id_seq")
    @SequenceGenerator(name = "comparison_id_seq", sequenceName = "comparison_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "comparison_institutions",
            joinColumns = @JoinColumn(name = "comparison_id"),
            inverseJoinColumns = @JoinColumn(name = "institution_id")
    )
    @Builder.Default
    private Set<Institution> institutions = new HashSet<>();

    private String requesterKey;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comparison)) return false;
        return id != null && id.equals(((Comparison) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
