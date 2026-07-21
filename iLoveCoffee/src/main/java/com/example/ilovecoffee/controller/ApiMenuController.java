package com.example.ilovecoffee.controller;

import com.example.ilovecoffee.dto.menu.response.MenuResponse;
import com.example.ilovecoffee.service.menu.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class ApiMenuController {
    private final MenuService menuService;

    @GetMapping("/{id}/detail")
    public ResponseEntity<MenuResponse> getMenu(
            @PathVariable Long id
    ) {
        MenuResponse response = menuService.findByIdForCustomer(id);
        return ResponseEntity.ok(response);
    }



}
