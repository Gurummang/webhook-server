package com.grummang.webhook_server.service.GoogleDrive;

import com.grummang.webhook_server.model.dto.google_drive.GoogleDriveChangeEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GoogleDriveDtoFunc {

    public GoogleDriveChangeEventDto convertToGoogleDriveChangeEventDto(
            String workspaceId, String channelId, String resourceId, String resourceState, String resourceUri) {
        GoogleDriveChangeEventDto dto = new GoogleDriveChangeEventDto();
        dto.setWorkspaceId(workspaceId);
        dto.setChannelId(channelId);
        dto.setResourceId(resourceId);
        dto.setResourceState(resourceState);
        dto.setResourceUri(resourceUri);
        return dto;
    }
}
