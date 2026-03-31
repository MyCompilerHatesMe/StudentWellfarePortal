package com.mchm.swp.model.dto.response;

import java.util.Set;

public record RegisterResponse(
        String username,
        Set<String> roles
) {
}
