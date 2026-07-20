package com.example.ilovecoffee.exception;

import org.springframework.http.HttpStatus;

public class MenuNotFoundException extends BusinessException {
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
    private static final String CODE = "MENU_NOT_FOUND";

    public MenuNotFoundException() {
        this("메뉴를 찾을 수 없습니다.");
    }
    public MenuNotFoundException(String message) {
        super(STATUS, CODE, message);
    }
}
