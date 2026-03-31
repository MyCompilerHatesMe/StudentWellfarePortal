package com.mchm.swp.model.dto.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ParentProfileResponse extends ProfileResponse {
    private String number;
}
