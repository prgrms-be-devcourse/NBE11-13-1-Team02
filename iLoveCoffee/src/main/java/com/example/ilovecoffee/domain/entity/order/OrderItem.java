package com.example.ilovecoffee.domain.entity.order;

import com.example.ilovecoffee.domain.entity.menu.Menu;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long menuId;
    private long menuVersion;
    private String name;
    private long price;
    private int quantity;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    public void assignOrder(Order order) {
        this.order = order;
    }

    public static OrderItem from(
            Menu menu,
            int quantity
    ) {
        return OrderItem.builder()
                .menuId(menu.getId())
                .menuVersion(menu.getVersion())
                .name(menu.getName())
                .price(menu.getPrice())
                .quantity(quantity)
                .build();
    }
    public static OrderItem of(
            Long menuId,
            Long menuVersion,
            String name,
            long price,
            int quantity
    ) {
        return OrderItem.builder()
                .menuId(menuId)
                .menuVersion(menuVersion)
                .name(name)
                .price(price)
                .quantity(quantity)
                .build();
    }

    public boolean isSameMenu(Long menuId) {
        return this.menuId.equals(menuId);
    }

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }

    public long calculateSubtotal() {
        return price * quantity;
    }

    public long getSubtotal() {
        return quantity * price;
    }

}
