package com.example.ilovecoffee.service.component;

import com.example.ilovecoffee.domain.entity.order.Order;
import com.example.ilovecoffee.domain.enums.OrderStatus;
import com.example.ilovecoffee.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OrderBatchManager {

    private final OrderRepository orderRepository;

    @Transactional
    public void confirmPendingOrders() {
        orderRepository.findAllByOrderStatus(OrderStatus.PENDING)
                .forEach(Order::confirm);
    }

    @Transactional
    public void prepareConfirmingOrders() {
        orderRepository.findAllByOrderStatus(OrderStatus.CONFIRMED)
                .forEach(Order::prepare);
    }

    @Transactional
    public void dispatchPreparingOrders() {
        orderRepository.findAllByOrderStatus(OrderStatus.PREPARING)
                .forEach(Order::dispatch);
    }

    @Transactional
    public void completeShippedOrders() {
        orderRepository.findAllByOrderStatus(OrderStatus.DISPATCHED)
                .forEach(Order::deliver);
    }

}
