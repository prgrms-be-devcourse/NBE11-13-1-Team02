package com.example.ilovecoffee.domain.entity.menu;

import com.example.ilovecoffee.domain.enums.MenuStatus;
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
public class MenuVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long menuId;
    private long version;

    private String name;
    private String description;
    private long price;
    private int stock;

    @Enumerated(EnumType.STRING)
    private MenuStatus status;
    private LocalDateTime archivedAt;

    public static MenuVersion from(Menu menu) {
        return MenuVersion.builder()
                .menuId(menu.getId())
                .version(menu.getVersion())
                .name(menu.getName())
                .description(menu.getDescription())
                .price(menu.getPrice())
                .stock(menu.getStock())
                .status(menu.getStatus())
                .archivedAt(LocalDateTime.now())
                .build();
    }
}
