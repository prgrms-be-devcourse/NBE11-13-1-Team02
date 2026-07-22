package com.example.ilovecoffee.dto.menu.response;

import com.example.ilovecoffee.constant.DateformatConstant;
import com.example.ilovecoffee.domain.enums.MenuStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AdminMenuResponse(
        Long id,
        String name,
        String description,
        String imageUrl,
        long price,
        int stock,
        String statusComment,
        MenuStatus status,
        @JsonFormat(pattern = DateformatConstant.DATE_FORMAT)
        LocalDateTime createdAt,
        @JsonFormat(pattern = DateformatConstant.DATE_FORMAT)
        LocalDateTime updatedAt,
        @JsonFormat(pattern = DateformatConstant.DATE_FORMAT)
        LocalDateTime deletedAt
) {
}
