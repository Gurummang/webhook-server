package com.grummang.webhook_server.service.GoogleDrive;

import com.grummang.webhook_server.model.dto.google_drive.GoogleDriveChangeEventDto;
import org.springframework.stereotype.Service;

public interface GoogleDriveEventHandler {
    void handleChangeEvent(GoogleDriveChangeEventDto changeEventDto);
}
