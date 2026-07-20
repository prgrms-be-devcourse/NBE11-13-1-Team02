package com.example.ilovecoffee.service.slack;

import com.example.ilovecoffee.dto.error.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class SlackNotificationService {

    @Value("${slack.webhook-url}")
    private String webhookUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    // 비동기(@Async)로 실행하여 메인 로직 멈춤 방지
    @Async
    public void sendSlackAlert(ErrorResponse errorResponse) {
        try {
            Map<String, Object> body = Map.of("text", "[ERROR]" + errorResponse);
            //System.out.println(errorResponse);
            restTemplate.postForEntity(webhookUrl, body, String.class);
        } catch (Exception e) {
            log.error("슬랙 전송 실패", e);
        }
    }
}