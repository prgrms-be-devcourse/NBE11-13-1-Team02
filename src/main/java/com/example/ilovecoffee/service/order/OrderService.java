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
import com.example.ilovecoffee.service.component.InventoryManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final InventoryManager inventoryManager;
    private final MenuRepository menuRepository;

    @Transactional
    public OrderResponse create(OrderRequest request) {
        log.info(
                "[주문 요청] email={}, postNumber={}, itemCount={}",
                request.email(),
                request.postNumber(),
                request.items().size()
        );

        Order order = findPendingOrderOrCreate(request);

        for (OrderItemRequest itemRequest : request.items()) {
            Menu menu = findMenu(itemRequest.menuId());

            log.debug(
                    "[주문 항목 처리] menuId={}, menuName={}, quantity={}, unitPrice={}",
                    menu.getId(),
                    menu.getName(),
                    itemRequest.quantity(),
                    menu.getPrice()
            );

            inventoryManager.decrease(
                    menu.getId(),
                    itemRequest.quantity()
            );

            log.debug(
                    "[재고 차감 완료] menuId={}, quantity={}",
                    menu.getId(),
                    itemRequest.quantity()
            );

            OrderItem orderItem = OrderItem.of(
                    menu.getId(),
                    menu.getVersion(),
                    menu.getName(),
                    menu.getPrice(),
                    itemRequest.quantity()
            );

            order.addItem(orderItem);
        }

        order.suspend();

        Order savedOrder = orderRepository.save(order);

        log.info(
                "[주문 저장 완료] orderId={}, email={}, status={}, totalPrice={}, itemCount={}",
                savedOrder.getId(),
                savedOrder.getEmail(),
                savedOrder.getOrderStatus(),
                savedOrder.getTotalPrice(),
                savedOrder.getItems().size()
        );

        return orderMapper.toOrderResponse(savedOrder);
    }

    public List<OrderResponse> findAll() {
        log.debug("[전체 주문 조회 요청]");

        List<OrderResponse> responses = orderRepository.findAll().stream()
                .map(orderMapper::toOrderResponse)
                .toList();

        log.debug("[전체 주문 조회 완료] count={}", responses.size());

        return responses;
    }

    public List<OrderResponse> findByEmail(String email) {
        log.debug("[이메일별 주문 조회 요청] email={}", email);

        List<OrderResponse> responses = orderRepository.findAllByEmail(email).stream()
                .map(orderMapper::toOrderResponse)
                .toList();

        log.debug(
                "[이메일별 주문 조회 완료] email={}, count={}",
                email,
                responses.size()
        );

        return responses;
    }

    public OrderResponse findById(Long id) {
        log.debug("[주문 상세 조회 요청] orderId={}", id);

        Order order = findOrder(id);

        log.debug(
                "[주문 상세 조회 완료] orderId={}, email={}, status={}",
                order.getId(),
                order.getEmail(),
                order.getOrderStatus()
        );

        return orderMapper.toOrderResponse(order);
    }

    @Transactional
    public void cancelOrder(Long id) {
        log.info("[주문 취소 요청] orderId={}", id);

        Order order = findOrder(id);

        log.debug(
                "[주문 취소 검증] orderId={}, currentStatus={}, itemCount={}",
                order.getId(),
                order.getOrderStatus(),
                order.getItems().size()
        );

        order.cancel();

        order.getItems().forEach(item -> {
            inventoryManager.restoreIfPresent(
                    item.getMenuId(),
                    item.getQuantity()
            );

            log.debug(
                    "[재고 복구 처리] orderId={}, menuId={}, quantity={}",
                    order.getId(),
                    item.getMenuId(),
                    item.getQuantity()
            );
        });

        log.info(
                "[주문 취소 완료] orderId={}, changedStatus={}",
                order.getId(),
                order.getOrderStatus()
        );
    }

    @Transactional
    public void deleteById(Long id) {
        log.info("[주문 삭제 요청] orderId={}", id);

        Order order = findOrder(id);

        log.debug(
                "[주문 삭제 검증] orderId={}, status={}",
                order.getId(),
                order.getOrderStatus()
        );

        order.validateDeletable();
        orderRepository.delete(order);

        log.info("[주문 삭제 완료] orderId={}", id);
    }

    @Transactional
    public void deleteAllByEmail(String email) {
        log.info("[이메일별 주문 전체 삭제 요청] email={}", email);

        List<Order> orders = orderRepository.findAllByEmail(email);

        log.debug(
                "[이메일별 삭제 대상 조회 완료] email={}, count={}",
                email,
                orders.size()
        );

        orders.forEach(order -> {
            log.debug(
                    "[주문 삭제 검증] orderId={}, status={}",
                    order.getId(),
                    order.getOrderStatus()
            );

            order.validateDeletable();
        });

        orderRepository.deleteAll(orders);

        log.info(
                "[이메일별 주문 전체 삭제 완료] email={}, deletedCount={}",
                email,
                orders.size()
        );
    }

    private Order findPendingOrderOrCreate(OrderRequest request) {
        return orderRepository
                .findFirstByEmailAndPostNumberAndAddressAndOrderStatusOrderByIdDesc(
                        request.email(),
                        request.postNumber(),
                        request.address(),
                        OrderStatus.PENDING
                )
                .map(order -> {
                    log.info(
                            "[주문 병합] 기존 대기 주문 사용 orderId={}, email={}, postNumber={}",
                            order.getId(),
                            order.getEmail(),
                            order.getPostNumber()
                    );

                    return order;
                })
                .orElseGet(() -> {
                    log.info(
                            "[신규 주문 생성] email={}, postNumber={}, address={}",
                            request.email(),
                            request.postNumber(),
                            request.address()
                    );

                    return Order.create(
                            request.email(),
                            request.postNumber(),
                            request.address()
                    );
                });
    }

    private Menu findMenu(Long menuId) {
        log.debug("[메뉴 조회] menuId={}", menuId);
        return menuRepository.findById(menuId)
                .orElseThrow(() -> menuNotFound(menuId));
    }

    private MenuNotFoundException menuNotFound(Long menuId) {
        log.warn("[메뉴 조회 실패] menuId={}", menuId);
        return new MenuNotFoundException();
    }

    private Order findOrder(Long id) {
        log.debug("[주문 조회] orderId={}", id);
        return orderRepository.findById(id)
                .orElseThrow(() -> orderNotFound(id));
    }

    private OrderNotFoundException orderNotFound(Long orderId) {
        log.warn("[주문 조회 실패] orderId={}", orderId);
        return new OrderNotFoundException();
    }
}