package com.example.ilovecoffee.dto.error;
import com.example.ilovecoffee.constant.DateformatConstant;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(
        int status,
        String code,
        String message,
        String api,
        String location,
        LocalDateTime time
) {

    @Override
    public String toString() {
        return """
            ```
            🚨 ERROR

            Status    : %d
            Code      : %s
            Message   : %s
            API       : %s
            Location  : %s
            Timestamp : %s
            ```
            """.formatted(
                status,
                code,
                message,
                api,
                location,
                time.format(DateformatConstant.DATE_FORMATTER)
        );
    }

    public static ErrorResponse of(
            int status,
            String code,
            String message,
            String api,
            String location,
            LocalDateTime time
    ) {
        return ErrorResponse.builder()
                .status(status)
                .code(code)
                .message(message)
                .api(api)
                .location(location)
                .time(time)
                .build();
    }
}
