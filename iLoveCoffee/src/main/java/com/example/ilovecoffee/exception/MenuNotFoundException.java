package com.example.ilovecoffee.exception;

import org.springframework.http.HttpStatus;

public class MenuNotFoundException extends BusinessException{

    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
    private static final String CODE = "MENU_NOT_FOUND";

    public MenuNotFoundException(Long id) {
        this("존재하지 않는 메뉴입니다. id=" + id);
    }

    public MenuNotFoundException(String message) {
        super(STATUS, CODE, message);
    }
}
