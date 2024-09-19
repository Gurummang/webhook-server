package com.grummang.webhook_server.model.dto.google_drive;

import lombok.Data;

@Data
public class GoogleDriveChangeEventDto {
    private String workspaceId;
    private String channelId;
    private String resourceId;
    private String resourceState;
    private String resourceUri;
}
