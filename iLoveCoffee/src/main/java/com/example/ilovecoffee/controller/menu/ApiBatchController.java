package com.example.ilovecoffee.controller.menu;

import com.example.ilovecoffee.service.component.OrderBatchManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
public class ApiBatchController {
    private final OrderBatchManager orderBatchManager;

    @PostMapping("/confirm")
    public ResponseEntity<Void> confirm() {
        orderBatchManager.confirmPendingOrders();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/prepare")
    public ResponseEntity<Void> prepare() {
        orderBatchManager.prepareConfirmingOrders();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/dispatch")
    public ResponseEntity<Void> dispatch() {
        orderBatchManager.dispatchPreparingOrders();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/complete")
    public ResponseEntity<Void> complete() {
        orderBatchManager.completeShippedOrders();
        return ResponseEntity.noContent().build();
    }
}
