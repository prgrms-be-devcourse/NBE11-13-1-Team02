package com.example.ilovecoffee.exception;

import org.springframework.http.HttpStatus;

public class AlreadyCanceledOrderException extends BusinessException {
    private static final HttpStatus STATUS = HttpStatus.CONFLICT;
    private static final String CODE = "ALREADY_CANCELED_ORDER";

    public AlreadyCanceledOrderException() {
        this("이미 취소된 주문입니다");
    }

    public AlreadyCanceledOrderException(String msg) {
        super(STATUS, CODE,  msg);
    }
}
