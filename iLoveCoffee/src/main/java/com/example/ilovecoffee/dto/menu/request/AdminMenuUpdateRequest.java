package com.example.ilovecoffee.dto.menu.request;

import lombok.Builder;

@Builder
public record AdminMenuUpdateRequest (
        String name,
        String description,
        int price,
        int stock,
        boolean isActive
) {
}
