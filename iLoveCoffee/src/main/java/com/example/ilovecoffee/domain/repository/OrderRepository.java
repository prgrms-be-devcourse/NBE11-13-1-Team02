package com.example.ilovecoffee.domain.repository;

import com.example.ilovecoffee.domain.entity.order.Order;
import com.example.ilovecoffee.domain.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByEmail(String email);
    List<Order> findAllByOrderStatus(OrderStatus status);
    Optional<Order> findFirstByEmailAndPostNumberAndAddressAndOrderStatusOrderByIdDesc(
            String email,
            String postNumber,
            String address,
            OrderStatus orderStatus
    );
}
