package com.grummang.webhook_server.service;

import com.grummang.webhook_server.dto.SlackChannelCreatedEventDto;
import com.grummang.webhook_server.dto.SlackEventDto;
import com.grummang.webhook_server.dto.SlackFileSharedEventDto;
import com.grummang.webhook_server.dto.SlackMemberJoinedChannelEventDto;

import java.util.Map;

public interface SlackEventHandler {
    void handleFileSharedEvent(SlackFileSharedEventDto eventDto);
    void handleMemberJoinedChannelEvent(SlackMemberJoinedChannelEventDto eventDto);
    void handleChannelCreatedEvent(SlackChannelCreatedEventDto eventDto);
    String handleEventSubscribed(Map<String, Object> payload);
}