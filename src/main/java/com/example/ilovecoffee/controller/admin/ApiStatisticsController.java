package com.example.ilovecoffee.controller.admin;

import com.example.ilovecoffee.dto.order.response.DailySalesResponse;
import com.example.ilovecoffee.service.admin.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/statistics")
@RequiredArgsConstructor
public class ApiStatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/daily")
    public ResponseEntity<DailySalesResponse> findDailySales() {
        return ResponseEntity.ok(
                statisticsService.findDailySales()
        );
    }
}