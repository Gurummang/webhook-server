package com.grummang.webhook_server.service.O365;

import com.grummang.webhook_server.model.dto.o365.OneDriveFileChangeEventDto;
import com.grummang.webhook_server.model.dto.slack.*;

import java.util.Map;

public interface O365EventHandler {
//    void handleFileCreatedEvent(SlackFileSharedEventDto eventDto);

    void handleFileChangedEvent(OneDriveFileChangeEventDto userLeftEventDto);

//    void handleFileDeletedEvent(SlackFileDeletedEventDto channelDeletedEventDto);
}