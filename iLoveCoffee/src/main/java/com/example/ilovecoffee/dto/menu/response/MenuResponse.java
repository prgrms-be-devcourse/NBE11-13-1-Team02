package com.example.ilovecoffee.dto.menu.response;

import com.example.ilovecoffee.domain.entity.menu.MenuStatus;
import lombok.Builder;

@Builder
public record MenuResponse(
        Long id,
        String name,
        String description,
        long price,
        int stock,
        boolean soldOut
) {

}
