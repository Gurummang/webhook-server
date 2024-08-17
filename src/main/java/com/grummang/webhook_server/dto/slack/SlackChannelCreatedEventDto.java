package com.grummang.webhook_server.dto.slack;

import lombok.Data;

@Data
public class SlackChannelCreatedEventDto {
    private String from;
    private String event;
    private String saas;
    private String teamId;
    private String channelId;
}
