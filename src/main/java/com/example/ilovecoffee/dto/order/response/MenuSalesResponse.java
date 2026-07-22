package com.example.ilovecoffee.dto.order.response;

public record MenuSalesResponse(
        String menuName,
        long quantity,
        long sales
) {
}