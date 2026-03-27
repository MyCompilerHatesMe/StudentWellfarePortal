package com.mchm.swp.model.dto.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class StudentProfileResponse extends ProfileResponse {
    private String rollNo;
}
