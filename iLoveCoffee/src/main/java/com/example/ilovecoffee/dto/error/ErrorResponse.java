package com.example.ilovecoffee.dto.error;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(
        int status,
        String code,
        String message,
        LocalDateTime time
) {
    public static ErrorResponse of(
            int status,
            String code,
            String message,
            LocalDateTime time
    ) {
        return ErrorResponse.builder()
                .status(status)
                .code(code)
                .message(message)
                .time(time)
                .build();
    }
}
