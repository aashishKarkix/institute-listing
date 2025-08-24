package com.institute.listing.core.service;

import com.institute.listing.core.dto.CommentDTO;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;

public interface CommentService {

    List<CommentDTO> getCommentsByReview(Long reviewId);
    List<CommentDTO> getCommentsByInstitution(Long institutionId);

    CommentDTO addCommentToReview(Long reviewId, CommentDTO dto, OAuth2User oauthUser);
    CommentDTO addCommentToInstitution(Long institutionId, CommentDTO dto, OAuth2User oauthUser);

    CommentDTO updateComment(Long commentId, CommentDTO dto, OAuth2User oauthUser);
    void deleteComment(Long commentId, OAuth2User oauth2User);
}
