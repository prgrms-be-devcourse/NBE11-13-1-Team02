package com.example.ilovecoffee.dto.error;
import com.example.ilovecoffee.constant.DateformatConstant;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(
        int status,
        String code,
        String message,
        LocalDateTime time
) {
    @Override
    public String toString() {
        return toFormat();
    }

    private String toFormat() {
        return String.format(
                "\n%-10s: %s%n" + "%-10s: %s%n" + "%-10s: %s",
                "errorcode", code,
                "message", message,
                "timestamp", time.format(DateformatConstant.DATE_FORMATTER)
        );
    }
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
