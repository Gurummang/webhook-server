package com.grummang.webhook_server.dto;

import lombok.Data;

@Data
public class SlackMemberJoinedChannelEventDto {
    private String event;
    private String saas;
    private String token;
    private String teamId;
    private String apiAppId;
    private String joinedUser;
    private String joinedChannel;
    private String timestamp;
}
