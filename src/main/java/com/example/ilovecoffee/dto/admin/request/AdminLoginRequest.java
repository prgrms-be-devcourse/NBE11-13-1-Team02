package com.example.ilovecoffee.dto.admin.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AdminLoginRequest (

        @NotBlank
        String username,

        @NotBlank
        String password
) {
}