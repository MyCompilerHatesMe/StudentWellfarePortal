package com.mchm.swp.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public record WardConnectionRequest(
        @NotBlank(message = "Student username must not be blank") String studentUsername,
        @NotBlank(message = "Faculty username must not be blank") String parentUsername
) {
}
