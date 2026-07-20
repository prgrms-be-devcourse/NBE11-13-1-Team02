package com.example.ilovecoffee.dto.menu.request;

import com.example.ilovecoffee.domain.enums.MenuStatus;
import lombok.Builder;

@Builder
public record AdminMenuUpdateRequest (
        String name,
        String description,
        long price,
        int stock
) {
}
