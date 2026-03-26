package com.mchm.swp.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class RegisterResponse {
    private String user;
    private Set<String> roles;
}
