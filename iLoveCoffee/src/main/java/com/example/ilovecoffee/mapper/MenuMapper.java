package com.example.ilovecoffee.mapper;

import com.example.ilovecoffee.domain.entity.menu.Menu;
import com.example.ilovecoffee.dto.menu.response.AdminMenuResponse;
import com.example.ilovecoffee.dto.menu.response.MenuResponse;
import org.springframework.stereotype.Component;

@Component
public class MenuMapper {
    public MenuResponse toMenuResponse(Menu menu) {
        return MenuResponse.builder()
                .id(menu.getId())
                .name(menu.getName())
                .description(menu.getDescription())
                .price(menu.getPrice())
                .stock(menu.getStock())
                .isActive(menu.isActive())
                .build();
    }

    public AdminMenuResponse toAdminResponse(Menu menu) {
        return AdminMenuResponse.builder()
                .id(menu.getId())
                .name(menu.getName())
                .description(menu.getDescription())
                .price(menu.getPrice())
                .stock(menu.getStock())
                .isActive(menu.isActive())
                .createdAt(menu.getCreatedAt())
                .deletedAt(menu.getDeletedAt())
                .build();
    }
}
