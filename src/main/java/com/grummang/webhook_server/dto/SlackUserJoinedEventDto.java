package com.grummang.webhook_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class SlackUserJoinedEventDto {
    private String from;
    private String event;
    private String saas;
    private String joinedUserId;
}
