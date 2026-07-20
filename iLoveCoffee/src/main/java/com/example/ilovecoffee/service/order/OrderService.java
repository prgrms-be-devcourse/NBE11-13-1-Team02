package com.example.ilovecoffee.service.order;

import com.example.ilovecoffee.domain.entity.menu.Menu;
import com.example.ilovecoffee.domain.entity.order.Order;
import com.example.ilovecoffee.domain.entity.order.OrderItem;
import com.example.ilovecoffee.domain.enums.OrderStatus;
import com.example.ilovecoffee.domain.repository.MenuRepository;
import com.example.ilovecoffee.domain.repository.OrderRepository;
import com.example.ilovecoffee.dto.order.request.OrderItemRequest;
import com.example.ilovecoffee.dto.order.request.OrderRequest;
import com.example.ilovecoffee.dto.order.response.OrderResponse;
import com.example.ilovecoffee.exception.MenuNotFoundException;
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
    private final MenuRepository menuRepository;

    @Transactional
    public OrderResponse create(OrderRequest request) {
        List<OrderItem> orderItems = request.items().stream()
                .map(this::createOrderItem)
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
        Order order = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);



    }

    @Transactional
    public void deleteById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);
        orderRepository.delete(order);
    }

    @Transactional
    public void deleteAllByEmail(String email) {
        var orders = orderRepository.findAllByEmail(email);
        orderRepository.deleteAll(orders);
    }

    private OrderItem createOrderItem(OrderItemRequest request) {
        Menu menu = menuRepository.findById(request.menuId())
                .orElseThrow(MenuNotFoundException::new);
        menu.decrease(request.quantity());
        return OrderItem.from(menu, request.quantity());
    }
}
