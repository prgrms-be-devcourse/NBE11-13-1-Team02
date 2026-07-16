package com.example.ilovecoffee.dto.menu.response;

import com.example.ilovecoffee.domain.entity.menu.MenuStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AdminMenuResponse(
        Long id,
        String name,
        String description,
        int price,
        int stock,
        MenuStatus menuStatus,
        LocalDateTime createdAt,
        LocalDateTime deletedAt
) {
}
