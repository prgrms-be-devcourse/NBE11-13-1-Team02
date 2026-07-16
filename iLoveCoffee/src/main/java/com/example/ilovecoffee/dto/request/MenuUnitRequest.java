package com.example.ilovecoffee.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@RequiredArgsConstructor
public class MenuUnitRequest {
    private final Long id;
    private final String name;
    private final int price;
    private final String description;
    private final int stock;
    private final boolean manuallySoldOut;

    private final LocalDateTime createdAt;
    private final LocalDateTime deletedAt;

}
