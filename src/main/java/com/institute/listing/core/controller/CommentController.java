package com.institute.listing.core.controller;

import com.institute.listing.core.dto.CommentDTO;
import com.institute.listing.core.security.annotation.User;
import com.institute.listing.core.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<CommentDTO> addCommentToReview(@PathVariable Long reviewId, @RequestBody CommentDTO dto, Authentication authentication) {
        CommentDTO saved = commentService.addCommentToReview(reviewId, dto, authentication);
        return ResponseEntity.created(URI.create("/api/comments/" + saved.getId())).body(saved);
    }

    @User
    @PostMapping("/institutions/{institutionId}")
    public ResponseEntity<CommentDTO> addCommentToInstitution(@PathVariable Long institutionId, @RequestBody CommentDTO dto, Authentication authentication) {
        CommentDTO saved = commentService.addCommentToInstitution(institutionId, dto, authentication);
        return ResponseEntity.created(URI.create("/api/comments/" + saved.getId())).body(saved);
    }

    @User
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long commentId, @RequestBody CommentDTO dto, Authentication authentication) {
        return ResponseEntity.ok(commentService.updateComment(commentId, dto, authentication));
    }

    @User
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, Authentication authentication) {
        commentService.deleteComment(commentId, authentication);
        return ResponseEntity.noContent().build();
    }
}
