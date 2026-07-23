package com.example.ilovecoffee.service.admin;

import com.example.ilovecoffee.domain.entity.order.Order;
import com.example.ilovecoffee.domain.entity.order.OrderItem;
import com.example.ilovecoffee.domain.repository.OrderRepository;
import com.example.ilovecoffee.dto.order.response.DailySalesResponse;
import com.example.ilovecoffee.dto.order.response.MenuSalesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsService {

    private final OrderRepository orderRepository;

    public DailySalesResponse findDailySales() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        log.debug(
                "[일일 매출 조회 요청] start={}, end={}",
                start,
                end
        );

        List<Order> orders =
                orderRepository.findAllByOrderAtGreaterThanEqualAndOrderAtLessThan(
                        start,
                        end
                );

        long totalSales = orders.stream()
                .mapToLong(Order::getTotalPrice)
                .sum();

        long orderCount = orders.size();

        Map<String, MenuSalesAccumulator> accumulator =
                new LinkedHashMap<>();

        for (Order order : orders) {
            for (OrderItem item : order.getItems()) {
                accumulator.computeIfAbsent(
                        item.getName(),
                        ignored -> new MenuSalesAccumulator()
                ).add(item);
            }
        }

        List<MenuSalesResponse> details = accumulator.entrySet()
                .stream()
                .map(entry -> new MenuSalesResponse(
                        entry.getKey(),
                        entry.getValue().quantity,
                        entry.getValue().sales
                ))
                .toList();

        log.debug(
                "[일일 매출 조회 완료] orderCount={}, totalSales={}, menuCount={}",
                orderCount,
                totalSales,
                details.size()
        );

        return new DailySalesResponse(
                totalSales,
                orderCount,
                details
        );
    }

    private static class MenuSalesAccumulator {

        private long quantity;
        private long sales;

        private void add(OrderItem item) {
            quantity += item.getQuantity();
            sales += item.getPrice() * item.getQuantity();
        }
    }
}