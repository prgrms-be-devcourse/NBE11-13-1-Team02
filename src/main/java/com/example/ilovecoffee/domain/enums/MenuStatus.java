package com.example.ilovecoffee.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MenuStatus {
    ACTIVE("판매 중"),
    INACTIVE("판매 중지"),
    DELETED("삭제");

    private final String comment;
}
