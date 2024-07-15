package com.grummang.webhook_server.dto;

import lombok.Data;

@Data
public class SlackChannelCreatedEventDto {
    private String event;
    private String saas;
    private String token;
    private String teamId;
    private String apiAppId;
    private String channelId;
    private String channelName;
    private String channelType;
    private String timestamp;
}
