package com.example.ilovecoffee.exception;

import org.springframework.http.HttpStatus;

public class InsufficientStockException extends BusinessException {
    private static final HttpStatus STATUS = HttpStatus.CONFLICT;
    private static final String CODE = "INSUFFICIENT_STOCK";

    public InsufficientStockException() {
        this("재고가 부족합니다");
    }
    public InsufficientStockException(String message) {
        super(STATUS, CODE, message);
    }
}
