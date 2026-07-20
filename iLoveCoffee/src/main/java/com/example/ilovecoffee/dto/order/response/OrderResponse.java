package com.example.ilovecoffee.dto.order.response;

import com.example.ilovecoffee.constant.DateformatConstant;
import com.example.ilovecoffee.domain.enums.OrderStatus;
import com.example.ilovecoffee.domain.enums.ShipmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
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

        @JsonFormat(pattern = DateformatConstant.DATE_FORMAT)
        LocalDateTime orderAt,

        @JsonFormat(pattern = DateformatConstant.DATE_FORMAT)
        LocalDateTime dispatchAt,

        @JsonFormat(pattern = DateformatConstant.DATE_FORMAT)
        LocalDateTime deliveredAt
) {
}
