package com.example.ilovecoffee.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BusinessException {

    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;
    private static final String CODE = "Unauthorized";

    public UnauthorizedException() {
        this("인증이 필요합니다.");
    }

    public UnauthorizedException(String message) {
        super(STATUS, CODE, message);
    }
}
