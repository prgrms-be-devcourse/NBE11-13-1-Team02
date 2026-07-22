package com.example.ilovecoffee.dto.config.response;

public record ConfigResponse(
        String cutoffTime,
        long advanceDelayMs
) {

}