package com.example.ilovecoffee.controller.menu;

import com.example.ilovecoffee.dto.menu.response.MenuResponse;
import com.example.ilovecoffee.service.menu.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class ApiMenuController {
    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<List<MenuResponse>> getAllMenus() {
        var responses = menuService.findAllForCustomer();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<MenuResponse> getMenuDetail(
            @PathVariable Long id
    ) {
        MenuResponse response = menuService.findByIdForCustomer(id);
        return ResponseEntity.ok(response);
    }
}
