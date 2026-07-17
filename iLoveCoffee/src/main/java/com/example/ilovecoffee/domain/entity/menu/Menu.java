package com.example.ilovecoffee.domain.entity.menu;

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
    private String name;
    private String description;
    private long price;
    private int stock;
    @Enumerated(EnumType.STRING)
    private MenuStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    public void delete() {
        if(status == MenuStatus.DELETED) return;
        this.status = MenuStatus.DELETED;
        this.deletedAt = LocalDateTime.now();
    }
    public void update(
            String name,
            String description,
            long price,
            int stock,
            MenuStatus status
    ) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.status = status;
    }

    public void activate() {
        if(status == MenuStatus.ACTIVE) return;
        this.status = MenuStatus.ACTIVE;
        this.deletedAt = null;
    }

    public void deactivate() {
        if(status != MenuStatus.ACTIVE) return;
        this.status = MenuStatus.INACTIVE;
    }
}
