package com.example.ilovecoffee.service.order;

import com.example.ilovecoffee.domain.entity.menu.Menu;
import com.example.ilovecoffee.domain.entity.order.OrderItem;
import com.example.ilovecoffee.domain.repository.MenuRepository;
import com.example.ilovecoffee.exception.MenuNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final MenuRepository menuRepository;

    public OrderItem decrease(Long menuId, int quantity) {
        Menu menu = findMenu(menuId);
        menu.decrease(quantity);
        return OrderItem.from(menu, quantity);
    }

    public void increase(Long menuId, int quantity) {
        Menu menu = findMenu(menuId);
        menu.restoreStock(quantity);
    }

    public void restore(Long menuId, int quantity) {
        this.increase(menuId, quantity);
    }

    private Menu findMenu(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(MenuNotFoundException::new);
    }
}
