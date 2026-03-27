package com.mchm.swp.model.dto.response;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class ProfileResponse {
    private String name;
    private String email;
}
