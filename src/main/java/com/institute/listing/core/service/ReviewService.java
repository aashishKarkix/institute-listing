package com.institute.listing.core.service;

import com.institute.listing.core.dto.ReviewDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ReviewService {

    ReviewDTO createReview(ReviewDTO dto, Authentication user);

    ReviewDTO updateReview(Long id, ReviewDTO dto, Authentication user);

    void deleteReview(Long id, Authentication user);

    ReviewDTO getReview(Long id);

    List<ReviewDTO> getReviewsByInstitution(Long institutionId);

    List<ReviewDTO> getAllReviews();
}

