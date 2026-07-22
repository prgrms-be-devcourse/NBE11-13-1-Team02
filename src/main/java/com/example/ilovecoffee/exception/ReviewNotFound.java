package com.example.ilovecoffee.exception;

import org.springframework.http.HttpStatus;

public class ReviewNotFound extends BusinessException {
    public ReviewNotFound(HttpStatus status, String code, String message) {
        super(status, code, message);
    }

    public ReviewNotFound() {
        this(HttpStatus.NOT_FOUND, "REVIEW_NOT_FOUND", "REVIEW NOT FOUND");
    }
}
