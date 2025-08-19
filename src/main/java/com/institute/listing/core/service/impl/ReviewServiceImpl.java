package com.institute.listing.core.service.impl;

import com.institute.listing.ai.service.GeminiService;
import com.institute.listing.core.dto.ReviewDTO;
import com.institute.listing.core.exception.DuplicateReviewException;
import com.institute.listing.core.exception.NotFoundException;
import com.institute.listing.core.model.Institution;
import com.institute.listing.core.model.Review;
import com.institute.listing.core.model.User;
import com.institute.listing.core.repository.InstitutionRepository;
import com.institute.listing.core.repository.ReviewRepository;
import com.institute.listing.core.repository.UserRepository;
import com.institute.listing.core.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final InstitutionRepository institutionRepository;
    private final UserRepository userRepository;
    private final GeminiService geminiService;

    @Override
    public ReviewDTO createReview(ReviewDTO dto, OAuth2User oauthUser) {
        User authenticatedUser = getAuthenticatedUser(oauthUser);
        Institution institution = institutionRepository.findById(dto.getInstitutionId())
                .orElseThrow(() -> new NotFoundException("Institution not found"));

        if (reviewRepository.existsByUserIdAndInstitutionId(authenticatedUser.getId(), institution.getId())) {
            throw new DuplicateReviewException("You have already reviewed this institution");
        }

        String sentiment = geminiService.analyzeReviewSentiment(dto.getComment());
        if ("INVALID_FEEDBACK".equalsIgnoreCase(sentiment)) {
            throw new IllegalArgumentException("Review contains not proper comment");
        }

        Review review = Review.fromDTO(dto, authenticatedUser, institution);
        Review saved = reviewRepository.save(review);
        updateAvgRating(institution);

        return saved.toDTO();
    }

    @Override
    public ReviewDTO updateReview(Long id, ReviewDTO dto, OAuth2User oauthUser) {
        User authenticatedUser = getAuthenticatedUser(oauthUser);
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review not found"));

        if (!review.getUser().getId().equals(authenticatedUser.getId())) {
            throw new AccessDeniedException("You cannot update someone else's review");
        }

        String sentiment = geminiService.analyzeReviewSentiment(dto.getComment());
        if ("INVALID_FEEDBACK".equalsIgnoreCase(sentiment)) {
            throw new IllegalArgumentException("Review contains not proper comment");
        }

        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        Review updated = reviewRepository.save(review);
        updateAvgRating(updated.getInstitution());

        return updated.toDTO();
    }

    @Override
    public void deleteReview(Long id, OAuth2User oauthUser) {
        User authenticatedUser = getAuthenticatedUser(oauthUser);
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review not found"));

        if (!review.getUser().getId().equals(authenticatedUser.getId())) {
            throw new AccessDeniedException("You cannot delete someone else's review");
        }

        Institution institution = review.getInstitution();
        reviewRepository.delete(review);
        updateAvgRating(institution);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewDTO getReview(Long id) {
        return reviewRepository.findById(id)
                .map(Review::toDTO)
                .orElseThrow(() -> new NotFoundException("Review not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByInstitution(Long institutionId) {
        return reviewRepository.findByInstitutionId(institutionId)
                .stream()
                .map(Review::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getAllReviews() {
        return reviewRepository.findAll()
                .stream()
                .map(Review::toDTO)
                .toList();
    }

    private void updateAvgRating(Institution institution) {
        Double avg = reviewRepository.calculateAverageRatingByInstitutionId(institution.getId());
        institution.setAvgRating(avg != null ? avg : 0.0);
        institutionRepository.save(institution);
    }

    private User getAuthenticatedUser(OAuth2User oauthUser) {
        if (oauthUser == null) {
            throw new AccessDeniedException("User is not authenticated");
        }

        String email = oauthUser.getAttribute("email");
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

}
