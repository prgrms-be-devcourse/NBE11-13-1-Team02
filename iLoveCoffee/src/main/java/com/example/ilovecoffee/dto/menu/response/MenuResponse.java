package com.example.ilovecoffee.dto.menu.response;

import lombok.Builder;

@Builder
public record MenuResponse(
        Long id,
        String name,
        String description,
        int price,
        int stock,
        boolean isActive
) {

}
