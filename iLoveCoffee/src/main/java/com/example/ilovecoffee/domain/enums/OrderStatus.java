package com.example.ilovecoffee.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    PENDING("주문 대기 중"),
    CONFIRMED("주문 확정"),
    PREPARING("상품 준비 중"),
    DISPATCHED("출고 완료"),
    COMPLETED("상품 배송 완료");


    private final String comment;
}
