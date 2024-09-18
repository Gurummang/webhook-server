package com.grummang.webhook_server.service.GoogleDrive;

import com.grummang.webhook_server.model.dto.google_drive.GoogleDriveChangeEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GoogleDriveEventDistributor {

    private final GoogleDriveEventHandler googleDriveEventHandler;

    @Autowired
    public GoogleDriveEventDistributor(GoogleDriveEventHandler googleDriveEventHandler) {
        this.googleDriveEventHandler = googleDriveEventHandler;
    }

    public void distributeEvent(GoogleDriveChangeEventDto changeEventDto) {
        googleDriveEventHandler.handleChangeEvent(changeEventDto);
    }
}
