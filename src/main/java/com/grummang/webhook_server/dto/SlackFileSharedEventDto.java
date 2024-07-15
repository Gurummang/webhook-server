package com.grummang.webhook_server.dto;

import lombok.Data;

@Data
public class SlackFileSharedEventDto {
    private String event;
    private String saas;
    private String token;
    private String teamId;
    private String apiAppId;
    private String fileId;
    private String uploadUser;
    private String uploadChannel;
    private String timestamp;
}
