package com.example.ilovecoffee.service.component;

import com.example.ilovecoffee.constant.DateformatConstant;
import com.example.ilovecoffee.service.slack.SlackNotificationService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ServerNotifier {

    private static final long BYTE_TO_MB = 1024L * 1024L;

    private final SlackNotificationService slackNotificationService;
    private final Environment environment;

    private final Instant startedAt = Instant.now();

    @EventListener(ApplicationReadyEvent.class)
    public void notifyServerStart() {
        Runtime runtime = Runtime.getRuntime();

        String message = """
                🟢 *Server Start*
                ```
                Profile   : %s
                Java      : %s
                Memory    : %s
                StartedAt : %s
                ```
                """.formatted(
                getActiveProfiles(),
                System.getProperty("java.version"),
                getMemoryInfo(runtime),
                formatDateTime(LocalDateTime.now())
        );

        slackNotificationService.sendMessage(message);
    }

    @PreDestroy
    public void notifyServerStop() {
        Runtime runtime = Runtime.getRuntime();
        Duration uptime = Duration.between(startedAt, Instant.now());

        String message = """
                🛑 *Server Stop*
                ```
                Profile   : %s
                Java      : %s
                Memory    : %s
                Uptime    : %s
                StoppedAt : %s
                ```
                """.formatted(
                getActiveProfiles(),
                System.getProperty("java.version"),
                getMemoryInfo(runtime),
                formatDuration(uptime),
                formatDateTime(LocalDateTime.now())
        );

        slackNotificationService.sendMessage(message);
    }

    private String getActiveProfiles() {
        String[] profiles = environment.getActiveProfiles();

        if (profiles.length == 0) {
            return "default";
        }

        return String.join(", ", profiles);
    }

    private String getMemoryInfo(Runtime runtime) {
        long maxMemory = runtime.maxMemory() / BYTE_TO_MB;
        long totalMemory = runtime.totalMemory() / BYTE_TO_MB;
        long freeMemory = runtime.freeMemory() / BYTE_TO_MB;
        long usedMemory = totalMemory - freeMemory;

        double usage = (usedMemory * 100.0) / maxMemory;

        return "%dMB / %dMB (%.1f%%)"
                .formatted(usedMemory, maxMemory, usage);
    }

    private String formatDuration(Duration duration) {
        long days = duration.toDays();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        if (days > 0) {
            return "%dd %dh %dm %ds"
                    .formatted(days, hours, minutes, seconds);
        }
        if (hours > 0) {
            return "%dh %dm %ds"
                    .formatted(hours, minutes, seconds);
        }
        if (minutes > 0) {
            return "%dm %ds"
                    .formatted(minutes, seconds);
        }
        return "%ds".formatted(seconds);
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateformatConstant.DATE_FORMATTER);
    }
}