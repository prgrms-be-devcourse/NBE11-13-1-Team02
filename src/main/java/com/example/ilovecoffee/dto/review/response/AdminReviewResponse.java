package com.example.ilovecoffee.dto.review.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AdminReviewResponse (
    Long id,
    Long menuId,
    String menuName,
    Long menuVersion,
    String email,
    int rating,
    String content,
    LocalDateTime createdAt
) {}
