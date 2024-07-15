package com.grummang.webhook_server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlackEventDto {
    private String event;
    private String saas;
    private String token;
    private String teamId;
    private String apiAppId;
    private String fileId; // for file_shared event
    private String uploadUser; // for file_shared event
    private String uploadChannel; // for file_shared event
    private String joinedUser; // for member_joined_channel event
    private String joinedChannel; // for member_joined_channel event
    private String channelId; // for channel_created event
    private String channelName; // for channel_created event
    private String channelType; // for channel_created event
    private String timestamp;
}
