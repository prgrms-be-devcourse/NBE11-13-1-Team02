package com.example.ilovecoffee.dto.review.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReviewResponse(
    Long id,
    Long menuId,
    Long menuVersion,
    String email,
    int rating,
    String content,
    LocalDateTime createdAt
) {
}
