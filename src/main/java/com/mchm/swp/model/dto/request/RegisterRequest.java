package com.mchm.swp.model.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {
    private String name;
    private String password;
    private Set<String> roles;
}