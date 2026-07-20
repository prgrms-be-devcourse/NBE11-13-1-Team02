package com.example.ilovecoffee.exception;

import org.springframework.http.HttpStatus;

public class CannotDeleteOrderException extends BusinessException {
    private static final HttpStatus STATUS = HttpStatus.CONFLICT;
    private static final String CODE = "CANNOT_DELETE_ORDER";

    public CannotDeleteOrderException() {
        this("주문 내용을 삭제할 수 없습니다.");
    }

    public CannotDeleteOrderException(String msg) {
        super(STATUS, CODE,  msg);
    }
}