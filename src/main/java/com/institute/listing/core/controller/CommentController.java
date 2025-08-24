package com.institute.listing.core.controller;

import com.institute.listing.core.dto.CommentDTO;
import com.institute.listing.core.security.annotation.User;
import com.institute.listing.core.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByReview(@PathVariable Long reviewId) {
        return ResponseEntity.ok(commentService.getCommentsByReview(reviewId));
    }

    @GetMapping("/institutions/{institutionId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByInstitution(@PathVariable Long institutionId) {
        return ResponseEntity.ok(commentService.getCommentsByInstitution(institutionId));
    }

    @User
    @PostMapping("/reviews/{reviewId}")
    public ResponseEntity<CommentDTO> addCommentToReview(@PathVariable Long reviewId, @RequestBody CommentDTO dto, @AuthenticationPrincipal OAuth2User oauthUser) {
        CommentDTO saved = commentService.addCommentToReview(reviewId, dto, oauthUser);
        return ResponseEntity.created(URI.create("/api/comments/" + saved.getId())).body(saved);
    }

    @User
    @PostMapping("/institutions/{institutionId}")
    public ResponseEntity<CommentDTO> addCommentToInstitution(@PathVariable Long institutionId, @RequestBody CommentDTO dto, @AuthenticationPrincipal OAuth2User oauthUser) {
        CommentDTO saved = commentService.addCommentToInstitution(institutionId, dto, oauthUser);
        return ResponseEntity.created(URI.create("/api/comments/" + saved.getId())).body(saved);
    }

    @User
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long commentId, @RequestBody CommentDTO dto, @AuthenticationPrincipal OAuth2User oauthUser) {
        return ResponseEntity.ok(commentService.updateComment(commentId, dto, oauthUser));
    }

    @User
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal OAuth2User oauthUser) {
        commentService.deleteComment(commentId, oauthUser);
        return ResponseEntity.noContent().build();
    }
}
