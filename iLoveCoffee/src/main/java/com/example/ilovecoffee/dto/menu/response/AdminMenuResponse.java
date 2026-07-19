package com.example.ilovecoffee.dto.menu.response;

import com.example.ilovecoffee.domain.enums.MenuStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AdminMenuResponse(
        Long id,
        String name,
        String description,
        long price,
        int stock,
        MenuStatus status,
        LocalDateTime createdAt,
        LocalDateTime deletedAt
) {
}
