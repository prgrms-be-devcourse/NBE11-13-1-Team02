package com.example.ilovecoffee.domain.entity.order;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus shipmentStatus;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private long totalPrice;
    private List<OrderItem> items;

}
