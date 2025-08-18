package com.institute.listing.core.controller;

import com.institute.listing.core.dto.ReviewDTO;
import com.institute.listing.core.security.annotation.User;
import com.institute.listing.core.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService service;

    @User
    @PostMapping
    public ResponseEntity<ReviewDTO> create(@Valid @RequestBody ReviewDTO dto,
                                            @AuthenticationPrincipal OAuth2User oauthUser) {
        return ResponseEntity.ok(service.createReview(dto, oauthUser));
    }

    @User
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> update(@PathVariable Long id,
                                            @Valid @RequestBody ReviewDTO dto,
                                            @AuthenticationPrincipal OAuth2User oauthUser) {
        return ResponseEntity.ok(service.updateReview(id, dto, oauthUser));
    }

    @User
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @AuthenticationPrincipal OAuth2User oauthUser) {
        service.deleteReview(id, oauthUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getReview(id));
    }

    @GetMapping("/institution/{institutionId}")
    public ResponseEntity<List<ReviewDTO>> getByInstitution(@PathVariable Long institutionId) {
        return ResponseEntity.ok(service.getReviewsByInstitution(institutionId));
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAll() {
        return ResponseEntity.ok(service.getAllReviews());
    }
}
