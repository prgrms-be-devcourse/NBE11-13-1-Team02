package com.example.ilovecoffee.scheduler;

import com.example.ilovecoffee.service.component.OrderBatchManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderSchedulerTest {

    @Mock
    private OrderBatchManager orderBatchManager;

    @InjectMocks
    private OrderScheduler orderScheduler;

    @Nested
    @DisplayName("컷오프 확정 배치")
    class ConfirmCutoffOrders {

        @Test
        @DisplayName("대기 중인 주문을 확정 처리한다")
        void confirmCutoffOrders() {
            // when
            orderScheduler.confirmCutoffOrders();

            // then
            verify(orderBatchManager).confirmPendingOrders();
            verifyNoMoreInteractions(orderBatchManager);
        }
    }

    @Nested
    @DisplayName("자동 진행 배치")
    class AdvanceOrders {

        @Test
        @DisplayName("완료 -> 발송 -> 준비 순서로 역순 호출한다")
        void advanceOrdersCallsInReverseOrder() {
            // when
            orderScheduler.advanceOrders();

            // then
            InOrder inOrder = inOrder(orderBatchManager);
            inOrder.verify(orderBatchManager).completeShippedOrders();
            inOrder.verify(orderBatchManager).dispatchPreparingOrders();
            inOrder.verify(orderBatchManager).prepareConfirmingOrders();

            verifyNoMoreInteractions(orderBatchManager);
        }
    }
}