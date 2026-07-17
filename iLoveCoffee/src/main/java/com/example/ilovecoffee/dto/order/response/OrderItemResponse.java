package com.example.ilovecoffee.dto.order.response;

public record OrderItemResponse(
        Long menuId,
        String name,
        long price,
        int quantity,
        long subtotal
) {
}
