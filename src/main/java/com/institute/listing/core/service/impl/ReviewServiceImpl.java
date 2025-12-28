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
import com.institute.listing.core.service.ReviewService;
import com.institute.listing.core.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final InstitutionRepository institutionRepository;
    private final AuthUtil authUtil;
    private final GeminiService geminiService;
    private final ReviewSlackNotificationService slackNotificationService;

    @Override
    public ReviewDTO createReview(ReviewDTO dto, Authentication oauthUser) {
        User authenticatedUser = authUtil.getAuthenticatedUser(oauthUser);
        Institution institution = institutionRepository.findById(dto.getInstitutionId())
                .orElseThrow(() -> new NotFoundException("Institution not found"));

        if (reviewRepository.existsByUserIdAndInstitutionId(authenticatedUser.getId(), institution.getId())) {
            throw new DuplicateReviewException("You have already reviewed this institution");
        }

        validateSentiment(dto.getComment());

        Review review = Review.fromDTO(dto, authenticatedUser, institution);
        Review saved = reviewRepository.save(review);
        updateAvgRating(institution);

        slackNotificationService.sendReviewNotification(authenticatedUser, institution, saved, null, "created");
        return saved.toDTO();
    }

    @Override
    public ReviewDTO updateReview(Long id, ReviewDTO dto, Authentication user) {
        User authenticatedUser = authUtil.getAuthenticatedUser(user);
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review not found"));

        if (!review.getUser().getId().equals(authenticatedUser.getId())) {
            throw new AccessDeniedException("You cannot update someone else's review");
        }

        validateSentiment(dto.getComment());

        Review oldReview = review.toBuilder().build(); // copy old values

        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        Review updated = reviewRepository.save(review);
        updateAvgRating(updated.getInstitution());

        slackNotificationService.sendReviewNotification(authenticatedUser, updated.getInstitution(), updated, oldReview, "updated");
        return updated.toDTO();
    }

    @Override
    public void deleteReview(Long id, Authentication user) {
        User authenticatedUser = authUtil.getAuthenticatedUser(user);
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review not found"));

        if (!review.getUser().getId().equals(authenticatedUser.getId())) {
            throw new AccessDeniedException("You cannot delete someone else's review");
        }

        Institution institution = review.getInstitution();
        Review oldReview = review.toBuilder().build();

        reviewRepository.delete(review);
        updateAvgRating(institution);

        slackNotificationService.sendReviewNotification(authenticatedUser, institution, null, oldReview, "deleted");
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

    private void validateSentiment(String comment) {
        String sentiment = geminiService.analyzeReviewSentiment(comment);
        if ("INVALID_FEEDBACK".equalsIgnoreCase(sentiment)) {
            throw new IllegalArgumentException("Review contains invalid comment");
        }
    }

    private void updateAvgRating(Institution institution) {
        Double avg = reviewRepository.calculateAverageRatingByInstitutionId(institution.getId());
        institution.setAvgRating(avg != null ? avg : 0.0);
        institutionRepository.save(institution);
    }

}
