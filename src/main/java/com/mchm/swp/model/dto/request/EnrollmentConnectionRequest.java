package com.mchm.swp.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public record EnrollmentConnectionRequest(
        @NotBlank(message = "Student username must not be blank") String studentUsername,
        @NotBlank(message = "Faculty username must not be blank") String facultyUsername,
        @NotBlank(message = "Subject must not be blank") String subject
) {
}