package com.mchm.swp.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Login Request username must not be blank") String name,
        @NotBlank(message = "Login request password must not be blank") String password
) {
}