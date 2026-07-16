package com.example.ilovecoffee.domain.entity.menu;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = PROTECTED)
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private int price;
    private int stock;
    @Enumerated(EnumType.STRING)
    private MenuStatus menuStatus;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

}
