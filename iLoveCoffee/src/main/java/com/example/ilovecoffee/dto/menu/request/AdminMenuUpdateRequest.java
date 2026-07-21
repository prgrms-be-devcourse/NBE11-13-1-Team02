package com.example.ilovecoffee.dto.menu.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record AdminMenuUpdateRequest (
        @NotBlank(message = "메뉴 이름은 필수입니다.")
        @Size(max = 150, message = "메뉴 이름은 150자를 초과할 수 없습니다.")
        String name,

        @Size(max = 1000, message = "설명은 1000자를 초과할 수 없습니다.")
        String description,

        @Positive(message = "가격은 0보다 커야 합니다.")
        long price,

        @PositiveOrZero(message = "재고는 0 이상이어야 합니다.")
        int stock
) {
}
