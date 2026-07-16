package com.example.ilovecoffee.dto.menu.response;

import com.example.ilovecoffee.domain.entity.menu.MenuStatus;
import lombok.Builder;

@Builder
public record MenuResponse(
        Long id,
        String name,
        String description,
        int price,
        int stock,
        MenuStatus menuStatus
) {

}
