package com.example.ilovecoffee.exception;

import org.springframework.http.HttpStatus;

public class DuplicateReviewException extends BusinessException {
    public DuplicateReviewException(HttpStatus status, String code, String message) {
        super(status, code, message);
    }

    public DuplicateReviewException() {
        this(
                HttpStatus.CONFLICT,
                "REVIEW_CONFLICT",
                "REVIEW CONFLICT"
                );
    }
}
