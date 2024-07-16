package com.grummang.webhook_server.service;

import com.grummang.webhook_server.dto.SlackChannelCreatedEventDto;
import com.grummang.webhook_server.dto.SlackFileSharedEventDto;
import com.grummang.webhook_server.dto.SlackMemberJoinedChannelEventDto;
import com.grummang.webhook_server.dto.SlackUserJoinedEventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SlackEventDistributor {

    private final SlackEventHandler slackEventHandler;

    @Autowired
    public SlackEventDistributor(SlackEventHandler slackEventHandler) {
        this.slackEventHandler = slackEventHandler;
    }

    public void distributeEvent(SlackFileSharedEventDto eventDto) {
        slackEventHandler.handleFileSharedEvent(eventDto);
    }

    public void distributeEvent(SlackMemberJoinedChannelEventDto eventDto) {
        slackEventHandler.handleMemberJoinedChannelEvent(eventDto);
    }

    public void distributeEvent(SlackChannelCreatedEventDto eventDto) {
        slackEventHandler.handleChannelCreatedEvent(eventDto);
    }

    public void distributeEvent(SlackUserJoinedEventDto userJoinedEventDto) {
        slackEventHandler.handleUserJoinedEvent(userJoinedEventDto);
    }
}
