package com.example.ilovecoffee.dto.order.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

@Builder
public record OrderRequest(
        @NotBlank
        @Email
        String email,

        @NotBlank
        String postNumber,

        @NotBlank
        String address,

        @NotEmpty
        List<@Valid OrderItemRequest> items
) {
}
