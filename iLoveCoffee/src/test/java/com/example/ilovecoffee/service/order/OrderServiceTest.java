package com.example.ilovecoffee.service.order;

import com.example.ilovecoffee.domain.entity.order.Order;
import com.example.ilovecoffee.domain.entity.order.OrderItem;
import com.example.ilovecoffee.domain.enums.OrderStatus;
import com.example.ilovecoffee.domain.repository.OrderRepository;
import com.example.ilovecoffee.dto.order.request.OrderItemRequest;
import com.example.ilovecoffee.dto.order.request.OrderRequest;
import com.example.ilovecoffee.dto.order.response.OrderResponse;
import com.example.ilovecoffee.exception.OrderNotFoundException;
import com.example.ilovecoffee.mapper.OrderMapper;
import com.example.ilovecoffee.service.component.InventoryManager;
import com.example.ilovecoffee.service.component.OrderBatchManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private InventoryManager inventoryService;

    @InjectMocks
    private OrderBatchManager batchManager;

    @InjectMocks
    private OrderService orderService;

    @Nested
    @DisplayName("주문 생성")
    class Create {

        @Test
        @DisplayName("재고를 차감하고 주문을 저장한 뒤 응답을 반환한다")
        void createOrder() {
            // given
            OrderItemRequest firstRequest =
                    new OrderItemRequest(1L, 2);

            OrderItemRequest secondRequest =
                    new OrderItemRequest(2L, 3);

            OrderRequest request = new OrderRequest(
                    "coffee@test.com",
                    "12345",
                    "서울시 강남구",
                    List.of(firstRequest, secondRequest)
            );

            OrderItem firstOrderItem = mock(OrderItem.class);
            OrderItem secondOrderItem = mock(OrderItem.class);
            OrderResponse expectedResponse = mock(OrderResponse.class);

            when(inventoryService.decrease(1L, 2))
                    .thenReturn(firstOrderItem);

            when(inventoryService.decrease(2L, 3))
                    .thenReturn(secondOrderItem);

            when(orderRepository.save(any(Order.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            when(orderMapper.toOrderResponse(any(Order.class)))
                    .thenReturn(expectedResponse);

            // when
            OrderResponse result = orderService.create(request);

            // then
            assertThat(result).isSameAs(expectedResponse);

            verify(inventoryService).decrease(1L, 2);
            verify(inventoryService).decrease(2L, 3);
            verify(orderRepository).save(any(Order.class));
            verify(orderMapper).toOrderResponse(any(Order.class));
        }

        @Test
        @DisplayName("재고 차감 중 예외가 발생하면 주문을 저장하지 않는다")
        void doesNotSaveWhenInventoryDecreaseFails() {
            // given
            OrderRequest request = new OrderRequest(
                    "coffee@test.com",
                    "12345",
                    "서울시 강남구",
                    List.of(new OrderItemRequest(1L, 100))
            );

            RuntimeException exception = new RuntimeException("재고 부족");

            when(inventoryService.decrease(1L, 100))
                    .thenThrow(exception);

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isSameAs(exception);

            verify(orderRepository, never()).save(any(Order.class));
            verify(orderMapper, never()).toOrderResponse(any(Order.class));
        }
    }

    @Nested
    @DisplayName("주문 조회")
    class Find {

        @Test
        @DisplayName("전체 주문을 응답 DTO 목록으로 변환한다")
        void findAll() {
            // given
            Order firstOrder = mock(Order.class);
            Order secondOrder = mock(Order.class);

            OrderResponse firstResponse = mock(OrderResponse.class);
            OrderResponse secondResponse = mock(OrderResponse.class);

            when(orderRepository.findAll())
                    .thenReturn(List.of(firstOrder, secondOrder));

            when(orderMapper.toOrderResponse(firstOrder))
                    .thenReturn(firstResponse);

            when(orderMapper.toOrderResponse(secondOrder))
                    .thenReturn(secondResponse);

            // when
            List<OrderResponse> result = orderService.findAll();

            // then
            assertThat(result)
                    .containsExactly(firstResponse, secondResponse);

            verify(orderRepository).findAll();
            verify(orderMapper).toOrderResponse(firstOrder);
            verify(orderMapper).toOrderResponse(secondOrder);
        }

        @Test
        @DisplayName("이메일에 해당하는 주문을 응답 DTO 목록으로 변환한다")
        void findByEmail() {
            // given
            String email = "coffee@test.com";

            Order order = mock(Order.class);
            OrderResponse response = mock(OrderResponse.class);

            when(orderRepository.findAllByEmail(email))
                    .thenReturn(List.of(order));

            when(orderMapper.toOrderResponse(order))
                    .thenReturn(response);

            // when
            List<OrderResponse> result = orderService.findByEmail(email);

            // then
            assertThat(result).containsExactly(response);

            verify(orderRepository).findAllByEmail(email);
            verify(orderMapper).toOrderResponse(order);
        }

        @Test
        @DisplayName("해당 이메일의 주문이 없으면 빈 목록을 반환한다")
        void returnsEmptyListWhenEmailHasNoOrders() {
            // given
            String email = "empty@test.com";

            when(orderRepository.findAllByEmail(email))
                    .thenReturn(List.of());

            // when
            List<OrderResponse> result = orderService.findByEmail(email);

            // then
            assertThat(result).isEmpty();

            verifyNoInteractions(orderMapper);
        }
    }

    @Nested
    @DisplayName("주문 상태 일괄 변경")
    class ChangeStatus {

        @Test
        @DisplayName("대기 중인 주문을 승인한다")
        void confirmPendingOrders() {
            // given
            Order firstOrder = mock(Order.class);
            Order secondOrder = mock(Order.class);

            when(orderRepository.findAllByOrderStatus(OrderStatus.PENDING))
                    .thenReturn(List.of(firstOrder, secondOrder));

            // when
            batchManager.confirmPendingOrders();

            // then
            verify(firstOrder).confirm();
            verify(secondOrder).confirm();
        }

        @Test
        @DisplayName("승인된 주문의 제품 준비를 시작한다")
        void prepareConfirmingOrders() {
            // given
            Order order = mock(Order.class);

            when(orderRepository.findAllByOrderStatus(OrderStatus.CONFIRMED))
                    .thenReturn(List.of(order));

            // when
            batchManager.prepareConfirmingOrders();

            // then
            verify(order).prepare();
        }

        @Test
        @DisplayName("준비 완료된 주문을 발송한다")
        void dispatchPreparingOrders() {
            // given
            Order order = mock(Order.class);

            when(orderRepository.findAllByOrderStatus(OrderStatus.PREPARING))
                    .thenReturn(List.of(order));

            // when
            batchManager.dispatchPreparingOrders();

            // then
            verify(order).dispatch();
        }

        @Test
        @DisplayName("발송된 주문의 배송을 완료한다")
        void completeShippedOrders() {
            // given
            Order order = mock(Order.class);

            when(orderRepository.findAllByOrderStatus(OrderStatus.DISPATCHED))
                    .thenReturn(List.of(order));

            // when
            batchManager.completeShippedOrders();

            // then
            verify(order).deliver();
        }
    }

    @Nested
    @DisplayName("주문 취소")
    class Cancel {

        @Test
        @DisplayName("주문을 취소하고 모든 주문 항목의 재고를 복원한다")
        void cancelOrder() {
            // given
            Long orderId = 1L;

            Order order = mock(Order.class);
            OrderItem firstItem = mock(OrderItem.class);
            OrderItem secondItem = mock(OrderItem.class);

            when(orderRepository.findById(orderId))
                    .thenReturn(Optional.of(order));

            when(order.getItems())
                    .thenReturn(List.of(firstItem, secondItem));

            when(firstItem.getMenuId()).thenReturn(10L);
            when(firstItem.getQuantity()).thenReturn(2);

            when(secondItem.getMenuId()).thenReturn(20L);
            when(secondItem.getQuantity()).thenReturn(3);

            // when
            orderService.cancelOrder(orderId);

            // then
            verify(order).cancel();
            verify(inventoryService).restoreIfPresent(10L, 2);
            verify(inventoryService).restoreIfPresent(20L, 3);
        }

        @Test
        @DisplayName("주문이 존재하지 않으면 예외가 발생한다")
        void throwsWhenOrderDoesNotExist() {
            // given
            Long orderId = 999L;

            when(orderRepository.findById(orderId))
                    .thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> orderService.cancelOrder(orderId))
                    .isInstanceOf(OrderNotFoundException.class);

            verifyNoInteractions(inventoryService);
        }

        @Test
        @DisplayName("주문 취소가 실패하면 재고를 복원하지 않는다")
        void doesNotRestoreWhenCancelFails() {
            // given
            Long orderId = 1L;
            Order order = mock(Order.class);

            RuntimeException exception =
                    new RuntimeException("취소할 수 없는 주문");

            when(orderRepository.findById(orderId))
                    .thenReturn(Optional.of(order));

            doThrow(exception)
                    .when(order)
                    .cancel();

            // when & then
            assertThatThrownBy(() -> orderService.cancelOrder(orderId))
                    .isSameAs(exception);

            verifyNoInteractions(inventoryService);
            verify(order, never()).getItems();
        }
    }

    @Nested
    @DisplayName("주문 삭제")
    class Delete {

        @Test
        @DisplayName("삭제 가능한 주문을 검증한 후 삭제한다")
        void deleteById() {
            // given
            Long orderId = 1L;
            Order order = mock(Order.class);

            when(orderRepository.findById(orderId))
                    .thenReturn(Optional.of(order));

            // when
            orderService.deleteById(orderId);

            // then
            verify(order).validateDeletable();
            verify(orderRepository).delete(order);
        }

        @Test
        @DisplayName("주문이 존재하지 않으면 삭제하지 않고 예외가 발생한다")
        void throwsWhenDeletingMissingOrder() {
            // given
            Long orderId = 999L;

            when(orderRepository.findById(orderId))
                    .thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> orderService.deleteById(orderId))
                    .isInstanceOf(OrderNotFoundException.class);

            verify(orderRepository, never()).delete(any(Order.class));
        }

        @Test
        @DisplayName("삭제 검증에 실패하면 Repository에서 삭제하지 않는다")
        void doesNotDeleteWhenValidationFails() {
            // given
            Long orderId = 1L;
            Order order = mock(Order.class);

            RuntimeException exception =
                    new RuntimeException("삭제할 수 없는 주문");

            when(orderRepository.findById(orderId))
                    .thenReturn(Optional.of(order));

            doThrow(exception)
                    .when(order)
                    .validateDeletable();

            // when & then
            assertThatThrownBy(() -> orderService.deleteById(orderId))
                    .isSameAs(exception);

            verify(orderRepository, never()).delete(any(Order.class));
        }

        @Test
        @DisplayName("이메일에 해당하는 모든 주문을 검증한 후 삭제한다")
        void deleteAllByEmail() {
            // given
            String email = "coffee@test.com";

            Order firstOrder = mock(Order.class);
            Order secondOrder = mock(Order.class);
            List<Order> orders = List.of(firstOrder, secondOrder);

            when(orderRepository.findAllByEmail(email))
                    .thenReturn(orders);

            // when
            orderService.deleteAllByEmail(email);

            // then
            verify(firstOrder).validateDeletable();
            verify(secondOrder).validateDeletable();
            verify(orderRepository).deleteAll(orders);
        }

        @Test
        @DisplayName("일괄 삭제 중 하나라도 검증에 실패하면 전체 삭제하지 않는다")
        void doesNotDeleteAllWhenValidationFails() {
            // given
            String email = "coffee@test.com";

            Order firstOrder = mock(Order.class);
            Order secondOrder = mock(Order.class);
            List<Order> orders = List.of(firstOrder, secondOrder);

            RuntimeException exception =
                    new RuntimeException("삭제할 수 없는 주문");

            when(orderRepository.findAllByEmail(email))
                    .thenReturn(orders);

            doThrow(exception)
                    .when(secondOrder)
                    .validateDeletable();

            // when & then
            assertThatThrownBy(() -> orderService.deleteAllByEmail(email))
                    .isSameAs(exception);

            verify(firstOrder).validateDeletable();
            verify(secondOrder).validateDeletable();
            verify(orderRepository, never()).deleteAll(any());
        }

        @Test
        @DisplayName("삭제할 주문이 없어도 빈 목록으로 삭제 요청을 수행한다")
        void deleteAllWithEmptyList() {
            // given
            String email = "empty@test.com";
            List<Order> orders = List.of();

            when(orderRepository.findAllByEmail(email))
                    .thenReturn(orders);

            // when
            orderService.deleteAllByEmail(email);

            // then
            verify(orderRepository).deleteAll(orders);
        }
    }
}