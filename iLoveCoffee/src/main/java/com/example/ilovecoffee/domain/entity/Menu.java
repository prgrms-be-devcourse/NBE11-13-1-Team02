package com.example.ilovecoffee.domain.entity;

import lombok.*;

import java.time.LocalDateTime;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Menu {
    private Long id;
    private String name;
    private int price;
    private String description;
    private int stock;
    private boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
}
