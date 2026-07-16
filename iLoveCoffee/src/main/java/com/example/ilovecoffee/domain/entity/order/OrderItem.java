package com.example.ilovecoffee.domain.entity.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = PROTECTED)
public class OrderItem {

    private Long id;
    private String name;
    private long price;
    private int quantity;

}
