package com.example.ilovecoffee.domain.entity.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = PROTECTED)
public class Order {

    private Long id;
    private Shipment status;
    private int totalPrice;
    private List<OrderItem> items;

}
