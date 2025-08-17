package com.institute.listing.core.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution1_id", nullable = false)
    private Institution institution1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution2_id", nullable = false)
    private Institution institution2;

    @Column(nullable = false)
    private String winner; // can be institution1.name or institution2.name

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
