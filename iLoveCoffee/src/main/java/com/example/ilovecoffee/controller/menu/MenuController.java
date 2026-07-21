package com.example.ilovecoffee.controller.menu;

import com.example.ilovecoffee.constant.PathConstant;
import com.example.ilovecoffee.dto.menu.response.AdminMenuResponse;
import com.example.ilovecoffee.service.menu.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstant.API_HOME)
public class MenuController {

    private final MenuService menuService;

    @PatchMapping("/admin/menus/{id}/inactive")
    public ResponseEntity<Void> deactivateMenu(@PathVariable Long id) {
        menuService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/admin/menus/{id}/active")
    public ResponseEntity<Void> activateMenu(@PathVariable Long id) {
        menuService.activate(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/menus/recents")
    public List<AdminMenuResponse> getRecentlyDeletedMenus() {
        return menuService.findTrash();
    }

    @DeleteMapping("/admin/menus/recents/{id}/delete")
    public ResponseEntity<Void> hardDeleteMenu(@PathVariable Long id) {
        menuService.permanentlyDelete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/admin/menus/recents/{id}/restore")
    public ResponseEntity<Void> restoreMenu(@PathVariable Long id) {
        menuService.restore(id);
        return ResponseEntity.noContent().build();
    }
}
