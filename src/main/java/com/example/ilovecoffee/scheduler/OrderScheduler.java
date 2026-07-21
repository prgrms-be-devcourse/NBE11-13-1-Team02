package com.example.ilovecoffee.scheduler;

import com.example.ilovecoffee.service.component.OrderBatchManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderScheduler {

    private final OrderBatchManager orderBatchManager;

    @Scheduled(
            cron = "${order.batch.cutoff-cron}",
            zone = "Asia/Seoul"
    )
    public void confirmCutoffOrders() {
        log.info("[스케줄러] 주문 확정 처리");
        orderBatchManager.confirmPendingOrders();
    }

    @Scheduled(
            fixedDelayString = "${order.batch.advance-delay-ms}"
    )
    public void advanceOrders() {
        orderBatchManager.completeShippedOrders();
        orderBatchManager.dispatchPreparingOrders();
        orderBatchManager.prepareConfirmingOrders();
    }
}
