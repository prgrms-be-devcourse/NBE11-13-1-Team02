package com.example.ilovecoffee.dto.menu.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AdminMenuResponse(
        Long id,
        String name,
        String description,
        int price,
        int stock,
        boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime deletedAt
) {
}
