package com.grummang.webhook_server.dto.slack;

import lombok.Data;

@Data
public class SlackFileDeletedEventDto {

    private String from;
    private String event;
    private String saas;
    private String teamId;
    private String fileId;
}
