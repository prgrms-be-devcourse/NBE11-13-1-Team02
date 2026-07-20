package com.example.ilovecoffee.domain.repository;

import com.example.ilovecoffee.domain.entity.order.Order;
import com.example.ilovecoffee.domain.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByEmail(String email);
    List<Order> findAllByOrderStatus(OrderStatus status);
}
