package com.example.ilovecoffee.exception;

import org.springframework.http.HttpStatus;

public class MenuNotInTrashException extends BusinessException {

    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;
    private static final String CODE = "MENU_NOT_IN_TRASH";

    public MenuNotInTrashException() {
        this("휴지통에 있는 메뉴만 완전 삭제할 수 있습니다.");
    }

    public MenuNotInTrashException(String message) {
        super(STATUS, CODE, message);
    }
}
