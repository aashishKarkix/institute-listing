package com.institute.listing.core.service.impl;

import com.institute.listing.ai.service.GeminiService;
import com.institute.listing.core.dto.CommentDTO;
import com.institute.listing.core.exception.NotFoundException;
import com.institute.listing.core.model.Comment;
import com.institute.listing.core.model.Institution;
import com.institute.listing.core.model.Review;
import com.institute.listing.core.model.User;
import com.institute.listing.core.repository.CommentRepository;
import com.institute.listing.core.repository.InstitutionRepository;
import com.institute.listing.core.repository.ReviewRepository;
import com.institute.listing.core.service.CommentService;
import com.institute.listing.core.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final InstitutionRepository institutionRepository;
    private final AuthUtil authUtil;
    private final GeminiService geminiService;

    @Override
    @Transactional(readOnly = true)
    public List<CommentDTO> getCommentsByReview(Long reviewId) {
        return commentRepository.findByReviewIdOrderByCreatedAtDesc(reviewId)
                .stream()
                .map(Comment::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDTO> getCommentsByInstitution(Long institutionId) {
        return commentRepository.findByInstitutionIdOrderByCreatedAtDesc(institutionId)
                .stream()
                .map(Comment::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public CommentDTO addCommentToReview(Long reviewId, CommentDTO dto, Authentication authentication) {
        User user = authUtil.getAuthenticatedUser(authentication);
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found: " + reviewId));

        if (dto.getInstitutionId() != null) {
            throw new IllegalArgumentException("Comment on review must not carry institutionId");
        }

        validateSentiment(dto.getCommentText());

        Comment saved = commentRepository.save(Comment.fromDTO(dto, user, review, null));
        return saved.toDTO();
    }

    @Override
    @Transactional
    public CommentDTO addCommentToInstitution(Long institutionId, CommentDTO dto, Authentication authentication) {
        User user = authUtil.getAuthenticatedUser(authentication);
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new NotFoundException("Institution not found: " + institutionId));

        if (dto.getReviewId() != null) {
            throw new IllegalArgumentException("Comment on institution must not carry reviewId");
        }

        validateSentiment(dto.getCommentText());

        Comment saved = commentRepository.save(Comment.fromDTO(dto, user, null, institution));
        return saved.toDTO();
    }


    @Override
    @Transactional
    public CommentDTO updateComment(Long commentId, CommentDTO dto, Authentication authentication) {
        User user = authUtil.getAuthenticatedUser(authentication);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found: " + commentId));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You can only update your own comment");
        }

        validateSentiment(dto.getCommentText());

        comment.setCommentText(dto.getCommentText());
        return commentRepository.save(comment).toDTO();
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Authentication authentication) {
        User user = authUtil.getAuthenticatedUser(authentication);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found: " + commentId));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You can only delete your own comment");
        }

        commentRepository.deleteById(commentId);
    }

    private void validateSentiment(String comment) {
        String sentiment = geminiService.analyzeReviewSentiment(comment);
        if ("INVALID_FEEDBACK".equalsIgnoreCase(sentiment)) {
            throw new IllegalArgumentException("Review contains invalid comment");
        }
    }
}
