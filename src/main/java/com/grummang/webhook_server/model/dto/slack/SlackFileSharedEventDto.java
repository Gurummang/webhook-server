package com.grummang.webhook_server.model.dto.slack;

import lombok.Data;

@Data
public class SlackFileSharedEventDto {
    private String from;
    private String event;
    private String saas;
    private String teamId;
    private String fileId;
}
