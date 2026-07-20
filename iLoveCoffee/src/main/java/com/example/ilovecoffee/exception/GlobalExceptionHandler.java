package com.example.ilovecoffee.exception;

import com.example.ilovecoffee.dto.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> businessExceptionHandler(BusinessException e) {
        log.warn("Business Exception code={}, message={}", e.getCode(), e.getMessage());
        return ResponseEntity
                .status(e.getStatus())
                .body(
                        ErrorResponse.of(
                                e.getStatus().value(),
                                e.getCode(),
                                e.getMessage(),
                                LocalDateTime.now()
                        )
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> notDefinedExceptionHandler(Exception e) {
        log.error("Unexpected Error message={}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ErrorResponse.of(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "INTERNAL_SERVER_ERROR",
                                "INTERNAL_SERVER_ERROR",
                                LocalDateTime.now()
                        )
                );
    }
}
