package com.grummang.webhook_server.service.GoogleDrive;

import com.grummang.webhook_server.model.dto.google_drive.GoogleDriveChangeEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GoogleDriveEventDistributor {
    public void distributeEvent(GoogleDriveChangeEventDto changeEventDto) {
    }
}
