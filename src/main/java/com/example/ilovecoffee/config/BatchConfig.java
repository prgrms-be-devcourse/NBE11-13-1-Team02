package com.example.ilovecoffee.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class BatchConfig {

    @Value("${order.batch.cutoff-cron}")
    private String cutoffCron;

    @Value("${order.batch.advance-delay-ms}")
    private long advanceDelayMs;

    public String getCutoffTime() {
        String[] cron = cutoffCron.split("\\s+");

        if (cron.length < 3) {
            throw new IllegalStateException("잘못된 Cron 표현식입니다 : " + cutoffCron);
        }

        int minute = Integer.parseInt(cron[1]);
        int hour = Integer.parseInt(cron[2]);

        return "%02d:%02d".formatted(hour, minute);
    }
}