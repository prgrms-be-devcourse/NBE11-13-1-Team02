package com.example.ilovecoffee.dto.repsonse;

import com.example.ilovecoffee.domain.entity.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

public class OrderUnitResponse {
    private Long id; // PK
    private String email;
    private String address;
    private String postNumber;
    private List<OrderItem> orderList;
    private long totalPrice;
    private LocalDateTime orderAt;
}
