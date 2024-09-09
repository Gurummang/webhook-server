package com.grummang.webhook_server.service.O365;

import com.grummang.webhook_server.model.dto.o365.OneDriveFileChangeEventDto;
import com.grummang.webhook_server.model.dto.slack.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class O365EventDistributor {

    private final O365EventHandler oneDriveEventHandler;

    @Autowired
    public O365EventDistributor(O365EventHandler oneDriveEventHandler) {
        this.oneDriveEventHandler = oneDriveEventHandler;
    }

    public void distributeEvent(OneDriveFileChangeEventDto fileChangeEventDto) {
        oneDriveEventHandler.handleFileChangedEvent(fileChangeEventDto);
    }

}
