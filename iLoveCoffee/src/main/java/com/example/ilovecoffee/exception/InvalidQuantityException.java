package com.example.ilovecoffee.exception;

import org.springframework.http.HttpStatus;
public class InvalidQuantityException extends BusinessException {
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;
    private static final String CODE = "QUANTITY_ERROR";

    public InvalidQuantityException() {
        this("개수는 1개 이상이어야 합니다.");
    }
    public InvalidQuantityException(String message) {
        super(STATUS, CODE, message);
    }
}