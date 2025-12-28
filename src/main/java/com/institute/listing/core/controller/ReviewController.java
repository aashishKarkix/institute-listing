package com.institute.listing.core.controller;

import com.institute.listing.core.dto.ReviewDTO;
import com.institute.listing.core.security.annotation.User;
import com.institute.listing.core.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
                                            Authentication authentication) {
        return ResponseEntity.ok(service.createReview(dto, authentication));
    }

    @User
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> update(@PathVariable Long id,
                                            @Valid @RequestBody ReviewDTO dto,
                                            Authentication authentication) {
        return ResponseEntity.ok(service.updateReview(id, dto, authentication));
    }

    @User
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       Authentication authentication) {
        service.deleteReview(id, authentication);
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
