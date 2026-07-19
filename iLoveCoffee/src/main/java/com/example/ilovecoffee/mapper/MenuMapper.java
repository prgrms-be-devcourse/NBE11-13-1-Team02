package com.example.ilovecoffee.mapper;

import com.example.ilovecoffee.domain.entity.menu.Menu;
import com.example.ilovecoffee.domain.enums.MenuStatus;
import com.example.ilovecoffee.dto.menu.request.AdminMenuCreateRequest;
import com.example.ilovecoffee.dto.menu.request.AdminMenuUpdateRequest;
import com.example.ilovecoffee.dto.menu.response.AdminMenuResponse;
import com.example.ilovecoffee.dto.menu.response.MenuResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MenuMapper {

    public MenuResponse toMenuResponse(Menu menu) {
        return MenuResponse.builder()
                .id(menu.getId())
                .name(menu.getName())
                .description(menu.getDescription())
                .price(menu.getPrice())
                .stock(menu.getStock())
                .soldOut(isSoldOut(menu))
                .build();
    }

    public AdminMenuResponse toAdminMenuResponse(Menu menu) {
        return AdminMenuResponse.builder()
                .id(menu.getId())
                .name(menu.getName())
                .description(menu.getDescription())
                .price(menu.getPrice())
                .stock(menu.getStock())
                .status(menu.getStatus())
                .createdAt(menu.getCreatedAt())
                .deletedAt(menu.getDeletedAt())
                .build();
    }

    private boolean isSoldOut(Menu menu) {
        return menu.getStatus() != MenuStatus.ACTIVE;
    }

    public Menu toEntity(AdminMenuCreateRequest request) {
        return Menu.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .stock(request.stock())
                .status(MenuStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void updateEntity(
            Menu menu,
            AdminMenuUpdateRequest request
    ) {
        menu.update(
                request.name(),
                request.description(),
                request.price(),
                request.stock()
        );
    }

}
