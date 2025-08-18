package com.institute.listing.core.service;

import com.institute.listing.core.dto.ReviewDTO;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;

public interface ReviewService {

    ReviewDTO createReview(ReviewDTO dto, OAuth2User user);

    ReviewDTO updateReview(Long id, ReviewDTO dto, OAuth2User authenticatedUser);

    void deleteReview(Long id, OAuth2User authenticatedUser);

    ReviewDTO getReview(Long id);

    List<ReviewDTO> getReviewsByInstitution(Long institutionId);

    List<ReviewDTO> getAllReviews();
}

