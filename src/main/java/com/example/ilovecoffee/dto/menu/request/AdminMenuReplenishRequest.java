package com.example.ilovecoffee.dto.menu.request;

import jakarta.validation.constraints.Min;

public record AdminMenuReplenishRequest (
        @Min(1)
        int quantity
) {
}
