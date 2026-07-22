package com.example.ilovecoffee.dto.order.response;

import java.util.List;

public record DailySalesResponse(
        long totalSales,
        long orderCount,
        List<MenuSalesResponse> details
) {
}