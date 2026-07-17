package com.example.ilovecoffee.dto.order.response;

import com.example.ilovecoffee.domain.entity.order.OrderStatus;
import com.example.ilovecoffee.domain.entity.order.ShipmentStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String email,
        String address,
        String postNumber,

        List<OrderItemResponse> items,
        long totalPrice,

        OrderStatus orderStatus,
        ShipmentStatus shipmentStatus,

        LocalDateTime orderAt
) {
}
