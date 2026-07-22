package com.example.ilovecoffee.controller;

import com.example.ilovecoffee.config.BatchConfig;
import com.example.ilovecoffee.dto.config.response.ConfigResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/config")
public class ConfigController {

    private final BatchConfig batchConfig;

    @GetMapping("/order-batch")
    public ConfigResponse getConfig() {
        return new ConfigResponse(
                batchConfig.getCutoffTime(),
                batchConfig.getAdvanceDelayMs()
        );
    }
}