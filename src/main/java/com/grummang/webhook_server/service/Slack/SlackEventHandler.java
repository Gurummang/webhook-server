package com.grummang.webhook_server.service.Slack;

import com.grummang.webhook_server.model.dto.slack.*;

import java.util.Map;

public interface SlackEventHandler {
    void handleFileSharedEvent(SlackFileSharedEventDto eventDto);
    void handleMemberJoinedChannelEvent(SlackMemberJoinedChannelEventDto eventDto);
    void handleChannelCreatedEvent(SlackChannelCreatedEventDto eventDto);
    String handleEventSubscribed(Map<String, Object> payload);
    void handleUserJoinedEvent(SlackUserJoinedEventDto userJoinedEventDto);


    void handleFileChangedEvent(SlackFileChangeEventDto userLeftEventDto);

    void handleFileDeletedEvent(SlackFileDeletedEventDto channelDeletedEventDto);
}