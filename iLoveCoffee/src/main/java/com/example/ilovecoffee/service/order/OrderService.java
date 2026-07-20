package com.example.ilovecoffee.service.order;

import com.example.ilovecoffee.domain.entity.order.Order;
import com.example.ilovecoffee.domain.entity.order.OrderItem;
import com.example.ilovecoffee.domain.enums.OrderStatus;
import com.example.ilovecoffee.domain.repository.OrderRepository;
import com.example.ilovecoffee.dto.order.request.OrderRequest;
import com.example.ilovecoffee.dto.order.response.OrderResponse;
import com.example.ilovecoffee.exception.OrderNotFoundException;
import com.example.ilovecoffee.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;

    @Transactional
    public OrderResponse create(OrderRequest request) {
        List<OrderItem> orderItems = request.items().stream()
                .map(item -> inventoryService.decrease(
                        item.menuId(),
                        item.quantity()
                ))
                .toList();
        Order order = Order.create(
                request.email(),
                request.postNumber(),
                request.address(),
                orderItems
        );

        order.suspend();
        Order saved = orderRepository.save(order);
        return orderMapper.toOrderResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findByEmail(String email) {
        return orderRepository.findAllByEmail(email).stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

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

    @Transactional
    public void cancelOrder(Long id) {
        Order order = findOrder(id);
        order.cancel();

        order.getItems().forEach(item ->
                inventoryService.restore(
                        item.getMenuId(),
                        item.getQuantity()
                )
        );
    }


    @Transactional
    public void deleteById(Long id) {
        Order order = findOrder(id);
        order.validateDeletable();
        orderRepository.delete(order);
    }

    @Transactional
    public void deleteAllByEmail(String email) {
        var orders = orderRepository.findAllByEmail(email);
        orders.forEach(Order::validateDeletable);
        orderRepository.deleteAll(orders);
    }

    private Order findOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);
    }
}
