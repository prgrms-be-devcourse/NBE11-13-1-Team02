package com.example.ilovecoffee.exception;

import com.example.ilovecoffee.dto.error.ErrorResponse;
import com.example.ilovecoffee.service.slack.SlackNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final SlackNotificationService slackNotificationService;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> businessExceptionHandler(BusinessException e) {
        log.warn("Business Exception code={}, message={}", e.getCode(), e.getMessage());

        var response =  ErrorResponse.of(
                e.getStatus().value(),
                e.getCode(),
                e.getMessage(),
                LocalDateTime.now()
        );

        slackNotificationService.sendSlackAlert(response);
        return ResponseEntity
                .status(e.getStatus())
                .body(response);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> notDefinedExceptionHandler(Exception e) {
        log.error("Unexpected Error message={}", e.getMessage());

        var response = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                "INTERNAL_SERVER_ERROR",
                LocalDateTime.now()
        );
        slackNotificationService.sendSlackAlert(response);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}