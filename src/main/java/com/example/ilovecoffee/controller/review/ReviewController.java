package com.example.ilovecoffee.controller.review;

import com.example.ilovecoffee.dto.review.request.ReviewCreateRequest;
import com.example.ilovecoffee.dto.review.response.ReviewResponse;
import com.example.ilovecoffee.service.review.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menus/{menuId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> findAll(
            @PathVariable Long menuId
    ) {
        return ResponseEntity.ok(
                reviewService.findAllByMenuId(menuId)
        );
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> create(
            @PathVariable Long menuId,
            @Valid @RequestBody ReviewCreateRequest request
    ) {
        var response = reviewService.create(menuId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
