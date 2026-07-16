package com.example.ilovecoffee.domain.entity.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = PROTECTED)
public class Menu {

    private Long id;
    private String name;
    private String description;
    private int price;
    private int stock;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

}
