package com.grummang.webhook_server.dto;

import lombok.Data;

@Data
public class SlackFileSharedEventDto {
    private String from;
    private String event;
    private String saas;
    private String fileId;
}
