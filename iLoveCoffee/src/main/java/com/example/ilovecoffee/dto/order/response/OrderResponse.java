package com.example.ilovecoffee.dto.order.response;

import com.example.ilovecoffee.domain.enums.OrderStatus;
import com.example.ilovecoffee.domain.enums.ShipmentStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
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
