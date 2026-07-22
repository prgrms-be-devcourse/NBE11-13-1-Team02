package com.example.ilovecoffee.scheduler;

import com.example.ilovecoffee.service.component.OrderBatchManager;
import com.example.ilovecoffee.service.slack.SlackNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderScheduler {

    private final OrderBatchManager orderBatchManager;
    private final SlackNotificationService slackNotificationService;

    @Scheduled(
            cron = "${order.batch.cutoff-cron}",
            zone = "Asia/Seoul"
    )
    public void confirmCutoffOrders() {
        log.info("[스케줄러] 주문 확정 처리");
        int count = orderBatchManager.confirmPendingOrders();
        slackNotificationService.sendMessage("확정 처리: " + count + "건");
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
