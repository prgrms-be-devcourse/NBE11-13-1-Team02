package com.example.ilovecoffee.exception;

import org.springframework.http.HttpStatus;

public class OrderDeletionNotAllowedException extends BusinessException{
    private static final HttpStatus STATUS = HttpStatus.CONFLICT;
    private static final String CODE = "INVALID_ORDER_STATUS";

    public OrderDeletionNotAllowedException() {
        this("현재 주문 상태에서는 요청을 처리할 수 없습니다.");
    }

    public OrderDeletionNotAllowedException(String msg) {
        super(STATUS, CODE,  msg);
    }
}
