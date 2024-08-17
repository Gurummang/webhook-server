package com.grummang.webhook_server.service.Slack;

import com.grummang.webhook_server.dto.slack.*;
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

    public void distributeEvent(SlackFileChangeEventDto userLeftEventDto) {
        slackEventHandler.handleFileChangedEvent(userLeftEventDto);
    }

    public void distributeEvent(SlackFileDeletedEventDto channelDeletedEventDto) {
        slackEventHandler.handleFileDeletedEvent(channelDeletedEventDto);
    }
}
