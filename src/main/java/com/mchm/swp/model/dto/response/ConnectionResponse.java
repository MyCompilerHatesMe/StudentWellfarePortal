package com.mchm.swp.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConnectionResponse {
    private String studentUsername;
    private String facultyUsername;
    private String subject;
}
