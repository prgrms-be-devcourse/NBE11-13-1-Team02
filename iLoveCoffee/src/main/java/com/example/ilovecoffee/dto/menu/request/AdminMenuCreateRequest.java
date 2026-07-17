package com.example.ilovecoffee.dto.menu.request;

import lombok.Builder;

@Builder
public record AdminMenuCreateRequest(
        String name,
        String description,
        long price,
        int stock
) {
}
