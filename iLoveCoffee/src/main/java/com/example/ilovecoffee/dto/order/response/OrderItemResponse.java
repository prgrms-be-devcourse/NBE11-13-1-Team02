package com.example.ilovecoffee.dto.order.response;

import lombok.Builder;

@Builder
public record OrderItemResponse(
        Long menuId,
        String name,
        long price,
        int quantity,
        long subtotal
) {
}
