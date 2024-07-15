package com.grummang.webhook_server.controller;

import com.grummang.webhook_server.dto.SlackChannelCreatedEventDto;
import com.grummang.webhook_server.dto.SlackFileSharedEventDto;
import com.grummang.webhook_server.dto.SlackMemberJoinedChannelEventDto;
import com.grummang.webhook_server.service.SlackEventDistributor;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/webhook")
@Slf4j
public class WebhookController {

    private final SlackEventDistributor slackEventDistributor;

    @Autowired
    public WebhookController(SlackEventDistributor slackEventDistributor) {
        this.slackEventDistributor = slackEventDistributor;
    }

    @PostMapping("/slack")
    public ResponseEntity<String> handleSlackEvent(@Valid @RequestBody Map<String, Object> payload) {
        log.info("Received Slack event: {}", payload);
        try {
            String type = (String) payload.get("type");
            if ("url_verification".equals(type)) {
                String challenge = (String) payload.get("challenge");
                return ResponseEntity.ok(challenge);
            }

            String eventType = (String) ((Map<String, Object>) payload.get("event")).get("type");

            switch (eventType) {
                case "file_shared":
                    SlackFileSharedEventDto fileSharedEventDto = convertToFileSharedEventDto(payload);
                    slackEventDistributor.distributeEvent(fileSharedEventDto);
                    break;
                case "member_joined_channel":
                    SlackMemberJoinedChannelEventDto memberJoinedChannelEventDto = convertToMemberJoinedChannelEventDto(payload);
                    slackEventDistributor.distributeEvent(memberJoinedChannelEventDto);
                    break;
                case "channel_created":
                    SlackChannelCreatedEventDto channelCreatedEventDto = convertToChannelCreatedEventDto(payload);
                    slackEventDistributor.distributeEvent(channelCreatedEventDto);
                    break;
                default:
                    log.warn("Unsupported event type: {}", eventType);
            }

            return ResponseEntity.ok("Event received successfully");
        } catch (Exception e) {
            log.error("Error processing event", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing event");
        }
    }

    private SlackFileSharedEventDto convertToFileSharedEventDto(Map<String, Object> payload) {
        Map<String, Object> event = (Map<String, Object>) payload.get("event");
        SlackFileSharedEventDto dto = new SlackFileSharedEventDto();
        dto.setEvent((String) payload.get("type"));
        dto.setToken((String) payload.get("token"));
        dto.setTeamId((String) payload.get("team_id"));
        dto.setApiAppId((String) payload.get("api_app_id"));
        dto.setFileId((String) event.get("file_id"));
        dto.setUploadUser((String) event.get("user_id"));
        dto.setUploadChannel((String) event.get("channel_id"));
        dto.setTimestamp((String) event.get("event_ts"));
        return dto;
    }

    private SlackMemberJoinedChannelEventDto convertToMemberJoinedChannelEventDto(Map<String, Object> payload) {
        Map<String, Object> event = (Map<String, Object>) payload.get("event");
        SlackMemberJoinedChannelEventDto dto = new SlackMemberJoinedChannelEventDto();
        dto.setEvent((String) payload.get("type"));
        dto.setToken((String) payload.get("token"));
        dto.setTeamId((String) payload.get("team_id"));
        dto.setApiAppId((String) payload.get("api_app_id"));
        dto.setJoinedUser((String) event.get("user"));
        dto.setJoinedChannel((String) event.get("channel"));
        dto.setTimestamp((String) event.get("event_ts"));
        return dto;
    }

    private SlackChannelCreatedEventDto convertToChannelCreatedEventDto(Map<String, Object> payload) {
        Map<String, Object> event = (Map<String, Object>) payload.get("event");
        SlackChannelCreatedEventDto dto = new SlackChannelCreatedEventDto();
        dto.setEvent((String) payload.get("type"));
        dto.setToken((String) payload.get("token"));
        dto.setTeamId((String) payload.get("team_id"));
        dto.setApiAppId((String) payload.get("api_app_id"));
        dto.setChannelId((String) event.get("channel_id"));
        dto.setChannelName((String) event.get("channel_name"));
        dto.setChannelType((String) event.get("channel_type"));
        dto.setTimestamp((String) event.get("event_ts"));
        return dto;
    }
}
