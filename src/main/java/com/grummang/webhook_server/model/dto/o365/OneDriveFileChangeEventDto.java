package com.grummang.webhook_server.model.dto.o365;

import lombok.Data;


@Data
public class OneDriveFileChangeEventDto {
    private String userId;
    private String tenantId;
    private String chageType;
}
