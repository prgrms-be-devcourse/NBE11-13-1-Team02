package com.example.ilovecoffee.dto.error;

import lombok.Builder;

@Builder
public record ErrorResponse(
        int status,
        String message
) {
}
