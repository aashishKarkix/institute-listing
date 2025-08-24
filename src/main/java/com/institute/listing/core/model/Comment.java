package com.institute.listing.core.model;

import com.institute.listing.core.dto.CommentDTO;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "comments_id_seq")
    @SequenceGenerator(name = "comments_id_seq", sequenceName = "comments_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id")
    private Institution institution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "comment_text", nullable = false)
    private String commentText;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        return id != null && id.equals(((Comment) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public CommentDTO toDTO() {
        return CommentDTO.builder()
                .id(this.id)
                .reviewId(this.review != null ? this.review.getId() : null)
                .institutionId(this.institution != null ? this.institution.getId() : null)
                .userId(this.user != null ? this.user.getId() : null)
                .userName(this.user != null ? this.user.getName() : null)
                .commentText(this.commentText)
                .createdAt(this.createdAt)
                .build();
    }

    public static Comment fromDTO(CommentDTO dto, User user, Review review, Institution institution) {
        return Comment.builder()
                .id(dto.getId())
                .user(user)
                .review(review)
                .institution(institution)
                .commentText(dto.getCommentText())
                .build();
    }
}
