package com.example.ilovecoffee.controller.menu;

import com.example.ilovecoffee.dto.menu.request.AdminMenuCreateRequest;
import com.example.ilovecoffee.dto.menu.request.AdminMenuUpdateRequest;
import com.example.ilovecoffee.dto.menu.response.AdminMenuResponse;
import com.example.ilovecoffee.service.menu.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/menus")
@RequiredArgsConstructor
public class ApiMenuAdminController {
    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<List<AdminMenuResponse>> adminMenu() {
        var responses = menuService.findAllForAdmin();

        return ResponseEntity.ok(responses);
    }

    @PostMapping(
            value = "/add",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<AdminMenuResponse> createMenu(
            @Valid @RequestPart("request") AdminMenuCreateRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        AdminMenuResponse response = menuService.create(request, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(
            value = "/{id}/update",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<AdminMenuResponse> updateMenu(
            @PathVariable Long id,
            @Valid @RequestPart("request") AdminMenuUpdateRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        AdminMenuResponse response = menuService.update(id, request, image);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteMenu(
            @PathVariable Long id
    ) {
        menuService.softDelete(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/inactive")
    public ResponseEntity<Void> deactivateMenu(@PathVariable Long id) {
        menuService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<Void> activateMenu(@PathVariable Long id) {
        menuService.activate(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recents")
    public List<AdminMenuResponse> getRecentlyDeletedMenus() {
        return menuService.findTrash();
    }

    @DeleteMapping("/recents/{id}/delete")
    public ResponseEntity<Void> hardDeleteMenu(@PathVariable Long id) {
        menuService.permanentlyDelete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/recents/{id}/restore")
    public ResponseEntity<Void> restoreMenu(@PathVariable Long id) {
        menuService.restore(id);
        return ResponseEntity.noContent().build();
    }



}
