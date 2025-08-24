package com.institute.listing.core.repository;

import com.institute.listing.core.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByReviewIdOrderByCreatedAtDesc(Long reviewId);

    List<Comment> findByInstitutionIdOrderByCreatedAtDesc(Long institutionId);
}
