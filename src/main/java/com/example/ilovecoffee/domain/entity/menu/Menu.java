package com.example.ilovecoffee.domain.entity.menu;

import com.example.ilovecoffee.domain.enums.MenuStatus;
import com.example.ilovecoffee.exception.InsufficientStockException;
import com.example.ilovecoffee.exception.InvalidQuantityException;
import com.example.ilovecoffee.exception.MenuNotInTrashException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = PROTECTED)
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private long version = 1L;

    private String name;
    private String description;
    private String imageUrl;
    private long price;
    private int stock;
    @Enumerated(EnumType.STRING)
    private MenuStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public void softDelete() {
        if(status == MenuStatus.DELETED) return;
        this.status = MenuStatus.DELETED;
        this.deletedAt = LocalDateTime.now();
    }

    public void updateInfo(
            String name,
            String description,
            String imageUrl,
            long price
    ) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    public void updateStock(int newStock) {
        if(newStock < 0)
            throw new InvalidQuantityException();
        this.stock = newStock;
        this.updatedAt = LocalDateTime.now();
        if(stock == 0)
            deactivate();
    }

    public void activate() {
        if(status == MenuStatus.ACTIVE) return;
        if(stock <= 0) throw new InsufficientStockException();
        this.status = MenuStatus.ACTIVE;
        this.deletedAt = null;
    }

    public void deactivate() {
        if(status != MenuStatus.ACTIVE) return;
        this.status = MenuStatus.INACTIVE;
    }

    public void decrease(int quantity) {
        if(quantity <= 0) {
            throw new InvalidQuantityException();
        }
        if(stock < quantity) {
            throw new InsufficientStockException();
        }
        stock -= quantity;
        if(stock == 0) {
            deactivate();
        }
    }

    private void increase(int quantity) {
        if(quantity <= 0) {
            throw new InvalidQuantityException();
        }
        stock += quantity;
    }

    public void restoreStock(int quantity) {
        this.increase(quantity);
    }

    public void replenishStock(int quantity) {
        this.increase(quantity);
    }

    public void restore() {
        if (status != MenuStatus.DELETED) {
            throw new MenuNotInTrashException();
        }

        status = MenuStatus.INACTIVE;
        deletedAt = null;
    }
}
