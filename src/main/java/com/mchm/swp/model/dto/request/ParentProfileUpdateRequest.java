package com.mchm.swp.model.dto.request;

import jakarta.validation.constraints.NotNull;

public record ParentProfileUpdateRequest(
        @NotNull(message = "New name field may be empty but not null") String newName,
        @NotNull(message = "New email field may be empty but not null") String newEmail,
        @NotNull(message = "New phone field may be empty but not null") String newPhone
) {
}
