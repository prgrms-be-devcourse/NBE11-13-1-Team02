package com.example.ilovecoffee.exception;

import com.example.ilovecoffee.dto.error.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> businessExceptionHandler(BusinessException e) {
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
}
