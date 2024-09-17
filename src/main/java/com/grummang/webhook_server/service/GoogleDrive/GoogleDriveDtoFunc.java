package com.grummang.webhook_server.service.GoogleDrive;

import com.grummang.webhook_server.model.dto.google_drive.GoogleDriveChangeEventDto;
import org.springframework.stereotype.Service;

@Service
public class GoogleDriveDtoFunc {
    public GoogleDriveChangeEventDto convertToGoogleDriveChangeEventDto(String workspaceId, String channelId, String resourceId, String resourceState, String resourceUri) {
    }
}
