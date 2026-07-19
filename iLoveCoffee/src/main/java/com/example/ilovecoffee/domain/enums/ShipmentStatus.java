package com.example.ilovecoffee.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShipmentStatus {

    PENDING("대기"),
    SHIPPING("배송 중"),
    DELIVERED("배송 완료"),

    FAILED("배송 실패"),
    RESHIPPING("재배송"),

    RETURNING("반품 중"),
    RETURNED("반품 완료");

    private final String comment;
}
