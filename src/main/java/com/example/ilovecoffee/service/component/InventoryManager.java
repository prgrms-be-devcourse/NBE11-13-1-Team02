package com.example.ilovecoffee.service.component;

import com.example.ilovecoffee.domain.entity.menu.Menu;
import com.example.ilovecoffee.domain.entity.order.OrderItem;
import com.example.ilovecoffee.domain.repository.MenuRepository;
import com.example.ilovecoffee.exception.MenuNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryManager {
    private final MenuRepository menuRepository;

    public OrderItem decrease(Long menuId, int quantity) {
        Menu menu = findMenu(menuId);
        menu.decrease(quantity);
        return OrderItem.from(menu, quantity);
    }

    public void replenish(Menu menu, int quantity) {
        menu.replenishStock(quantity);
    }

    private Menu findMenu(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(MenuNotFoundException::new);
    }

    public void restoreIfPresent(Long menuId, int quantity) {
        menuRepository.findById(menuId)
                .ifPresent(menu -> menu.restoreStock(quantity));
    }
}
