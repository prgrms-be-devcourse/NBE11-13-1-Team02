package com.example.ilovecoffee.mapper;

import com.example.ilovecoffee.domain.entity.order.Order;
import com.example.ilovecoffee.domain.entity.order.OrderItem;
import com.example.ilovecoffee.dto.order.response.OrderItemResponse;
import com.example.ilovecoffee.dto.order.response.OrderResponse;

import java.util.List;

public class OrderMapper {
    private OrderItemResponse toOrderItemResponse(OrderItem item) {
        return OrderItemResponse.builder()
                .menuId(item.getMenuId())
                .name(item.getName())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .subtotal(item.getQuantity() * item.getPrice())
                .build();
    }

    public OrderResponse toOrderResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(this::toOrderItemResponse)
                .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .email(order.getEmail())
                .address(order.getAddress())
                .postNumber(order.getPostNumber())
                .items(items)
                .orderStatus(order.getOrderStatus())
                .shipmentStatus(order.getShipmentStatus())
                .build();
    }

}
