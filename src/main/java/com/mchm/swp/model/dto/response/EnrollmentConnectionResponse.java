package com.mchm.swp.model.dto.response;

public record EnrollmentConnectionResponse(
        String studentUsername,
        String facultyUsername,
        String subject
) {
}