package com.example.ilovecoffee.controller.review;

import com.example.ilovecoffee.dto.review.response.AdminReviewResponse;
import com.example.ilovecoffee.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/reviews")
public class AdminReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<AdminReviewResponse>> findAll() {
        return ResponseEntity.ok(reviewService.findAllForAdmin());
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long reviewId
    ) {
        reviewService.deleteByAdmin(reviewId);
        return ResponseEntity.noContent().build();
    }
}
