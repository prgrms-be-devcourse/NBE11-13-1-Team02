package com.example.ilovecoffee.dto.menu.request;

import lombok.Builder;

@Builder
public record AdminMenuCreateRequest(
        String name,
        String description,
        int price,
        int stock,
        boolean isActive
) {
}
