package com.example.ilovecoffee.dto.review.request;

import jakarta.validation.constraints.*;

public record ReviewCreateRequest(
        @NotBlank
        @Email
        String email,

        @Min(1)
        @Max(5)
        int rating,

        @Size(max = 1000)
        String content
) {
}
