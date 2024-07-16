package com.grummang.webhook_server.service;

import com.grummang.webhook_server.dto.*;

import java.util.Map;

public interface SlackEventHandler {
    void handleFileSharedEvent(SlackFileSharedEventDto eventDto);
    void handleMemberJoinedChannelEvent(SlackMemberJoinedChannelEventDto eventDto);
    void handleChannelCreatedEvent(SlackChannelCreatedEventDto eventDto);
    void handleUserJoinedEvent(SlackUserJoinedEventDto userJoinedEventDto);
}