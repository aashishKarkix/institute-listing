package com.institute.listing.core.service;

import com.institute.listing.core.dto.CommentDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CommentService {

    List<CommentDTO> getCommentsByReview(Long reviewId);
    List<CommentDTO> getCommentsByInstitution(Long institutionId);

    CommentDTO addCommentToReview(Long reviewId, CommentDTO dto, Authentication authentication);
    CommentDTO addCommentToInstitution(Long institutionId, CommentDTO dto, Authentication authentication);

    CommentDTO updateComment(Long commentId, CommentDTO dto, Authentication authentication);
    void deleteComment(Long commentId, Authentication authentication);
}
