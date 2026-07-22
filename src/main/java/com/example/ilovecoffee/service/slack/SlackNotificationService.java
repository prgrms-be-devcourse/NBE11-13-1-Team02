package com.example.ilovecoffee.service.slack;

import com.example.ilovecoffee.dto.error.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackNotificationService {

    @Value("${slack.webhook-url}")
    private String webhookUrl;

    private final RestTemplate restTemplate;
    /**
     * 에러 발생 시 Slack으로 알림을 전송한다.
     * 비동기로 실행되어 메인 요청 처리를 지연시키지 않는다.
     */
    @Async
    public void sendErrorNotification(ErrorResponse errorResponse) {
        log.debug(
                "[Slack 전송 요청] code={}, status={}",
                errorResponse.code(),
                errorResponse.status()
        );
        try {
            sendMessage(errorResponse.toString());
            log.debug(
                    "[Slack 전송 완료] code={}",
                    errorResponse.code()
            );
        } catch (Exception e) {
            log.error(
                    "[Slack 전송 실패] code={}",
                    errorResponse.code(),
                    e
            );
        }
    }

    /**
     * 일반 Slack 메시지 전송
     */
    @Async
    public void sendMessage(String message) {
        try {
            Map<String, Object> body = Map.of(
                    "text", message
            );
            restTemplate.postForEntity(
                    webhookUrl,
                    body,
                    String.class
            );
        } catch (Exception e) {
            log.error(
                    "[Slack 메시지 전송 실패]",
                    e
            );
        }
    }
}