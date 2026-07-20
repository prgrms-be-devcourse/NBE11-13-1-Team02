package com.example.ilovecoffee.domain.entity.order;

import com.example.ilovecoffee.domain.entity.menu.Menu;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    public long getSubtotal() {
        return quantity * price;
    }

}
