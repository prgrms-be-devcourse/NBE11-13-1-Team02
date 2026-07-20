package com.example.ilovecoffee.dto.order.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record OrderItemRequest(
        @NotNull
        Long menuId,
        @Min(1)
        int quantity
) {
}
