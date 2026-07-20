package com.example.ilovecoffee.exception;

import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends BusinessException {
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
    private static final String CODE = "ORDER_NOT_FOUND";

    public OrderNotFoundException() {
        this("해당 주문을 찾을 수 없습니다.");
    }
    public OrderNotFoundException(String message) {
        super(STATUS, CODE, message);
    }
}
