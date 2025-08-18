package com.institute.listing.core.repository;

import com.institute.listing.core.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByInstitutionId(Long institutionId);

    boolean existsByUserIdAndInstitutionId(Long userId, Long institutionId);
}