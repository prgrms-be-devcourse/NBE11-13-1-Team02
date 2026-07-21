package com.example.ilovecoffee.controller;

import com.example.ilovecoffee.dto.menu.request.AdminMenuCreateRequest;
import com.example.ilovecoffee.dto.menu.request.AdminMenuUpdateRequest;
import com.example.ilovecoffee.dto.menu.response.AdminMenuResponse;
import com.example.ilovecoffee.dto.menu.response.MenuResponse;
import com.example.ilovecoffee.service.menu.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/menus")
@RequiredArgsConstructor
public class ApiMenuAdminController {
    private final MenuService menuService;

    @PostMapping("/add")
    public ResponseEntity<AdminMenuResponse> createMenu(
            @Valid @RequestBody AdminMenuCreateRequest request
    ) {
        AdminMenuResponse response = menuService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<AdminMenuResponse> updateMenu(
            @PathVariable Long id,
            @Valid @RequestBody AdminMenuUpdateRequest request
    ) {
        AdminMenuResponse response = menuService.update(id, request);

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteMenu(
            @PathVariable Long id
    ) {
        menuService.softDelete(id);

        return ResponseEntity.noContent().build();
    }


}
