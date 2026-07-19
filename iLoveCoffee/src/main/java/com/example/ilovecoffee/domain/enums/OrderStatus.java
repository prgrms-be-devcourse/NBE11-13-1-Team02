package com.example.ilovecoffee.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    CONFIRMED("주문 확정"),
    PAID("결제 완료"),
    PREPARING("상품 준비 중"),
    COMPLETED("상품 배송 완료"),
    SHIPPED("출고 완료"),
    CANCELED("주문 취소"),
    REFUNDED("환불 완료");

    private final String comment;
}
