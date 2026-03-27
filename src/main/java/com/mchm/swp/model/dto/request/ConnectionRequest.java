package com.mchm.swp.model.dto.request;

import lombok.Data;

@Data
public class ConnectionRequest {
    private String studentUsername;
    private String facultyUsername;
    private String subject;
}
