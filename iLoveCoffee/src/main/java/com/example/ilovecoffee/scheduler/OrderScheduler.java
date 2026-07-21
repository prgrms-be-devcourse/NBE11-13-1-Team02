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

    // 오후 2시 대기 주문 확정 처리
    @Scheduled(cron = "${order.batch.cutoff-cron:0 0 14 * * *}")
    public void confirmCutoffOrders() {
        log.info("[스케줄러] 주문 확정 처리");
        orderBatchManager.confirmPendingOrders();
    }

    // 1분간격으로 핟단계씩 자동 진행
    @Scheduled(fixedRateString = "${order.batch.advance-interval-ms:60000}")
    public void advanceOrders() {
        orderBatchManager.completeShippedOrders();
        orderBatchManager.dispatchPreparingOrders();
        orderBatchManager.prepareConfirmingOrders();
    }
}
