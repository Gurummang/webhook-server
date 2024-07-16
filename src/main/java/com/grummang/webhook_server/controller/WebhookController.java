package com.grummang.webhook_server.controller;

import com.grummang.webhook_server.dto.SlackChannelCreatedEventDto;
import com.grummang.webhook_server.dto.SlackFileSharedEventDto;
import com.grummang.webhook_server.dto.SlackMemberJoinedChannelEventDto;
import com.grummang.webhook_server.dto.SlackUserJoinedEventDto;
import com.grummang.webhook_server.service.SlackEventDistributor;
import com.slack.api.Slack;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("/slack/{org_webhook_url}")
    public ResponseEntity<String> handleSlackEvent(@Valid @RequestBody Map<String, Object> payload,@PathVariable  String org_webhook_url) {
        log.info("Received Slack event: {}", payload);

        System.out.println(org_webhook_url);
        try {
            String type = (String) payload.get("type");
            if ("url_verification".equals(type)) {
                String challenge = (String) payload.get("challenge");
                return ResponseEntity.ok(challenge);
            }

            String eventType = (String) ((Map<String, Object>) payload.get("event")).get("type");

            switch (eventType) {
                case "file_shared" -> {
                    SlackFileSharedEventDto fileSharedEventDto = convertToFileSharedEventDto(payload, org_webhook_url);
                    slackEventDistributor.distributeEvent(fileSharedEventDto);
                }
                case "member_joined_channel" -> {
                    SlackMemberJoinedChannelEventDto memberJoinedChannelEventDto = convertToMemberJoinedChannelEventDto(payload, org_webhook_url);
                    slackEventDistributor.distributeEvent(memberJoinedChannelEventDto);
                }
                case "channel_created" -> {
                    SlackChannelCreatedEventDto channelCreatedEventDto = convertToChannelCreatedEventDto(payload, org_webhook_url);
                    slackEventDistributor.distributeEvent(channelCreatedEventDto);
                }
                case "team_join" -> {
                    SlackUserJoinedEventDto userJoinedEventDto = convertToUserJoinedEventDto(payload, org_webhook_url);
                    slackEventDistributor.distributeEvent(userJoinedEventDto);
                }
                default -> log.warn("Unsupported event type: {}", eventType);
            }

            return ResponseEntity.ok("Event received successfully");
        } catch (Exception e) {
            log.error("Error processing event", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing event");
        }
    }

    private SlackFileSharedEventDto convertToFileSharedEventDto(Map<String, Object> payload,String org_webhook_url) {
        Map<String, Object> event = (Map<String, Object>) payload.get("event");
        SlackFileSharedEventDto dto = new SlackFileSharedEventDto();
        dto.setFrom(org_webhook_url);
        dto.setEvent((String) event.get("type"));
        dto.setSaas("slack");
        dto.setFileId((String) event.get("file_id"));
        return dto;
    }

    private SlackMemberJoinedChannelEventDto convertToMemberJoinedChannelEventDto(Map<String, Object> payload, String org_webhook_url) {
        Map<String, Object> event = (Map<String, Object>) payload.get("event");
        SlackMemberJoinedChannelEventDto dto = new SlackMemberJoinedChannelEventDto();
        dto.setFrom(org_webhook_url);
        dto.setEvent((String) event.get("type"));
        dto.setToken((String) payload.get("token"));
        dto.setTeamId((String) payload.get("team_id"));
        dto.setApiAppId((String) payload.get("api_app_id"));
        dto.setJoinedUser((String) event.get("user"));
        dto.setJoinedChannel((String) event.get("channel"));
        dto.setTimestamp((String) event.get("event_ts"));
        return dto;
    }

    private SlackChannelCreatedEventDto convertToChannelCreatedEventDto(Map<String, Object> payload, String org_webhook_url) {
        Map<String, Object> event = (Map<String, Object>) payload.get("event");
        SlackChannelCreatedEventDto dto = new SlackChannelCreatedEventDto();
        dto.setFrom(org_webhook_url);
        dto.setEvent((String) event.get("type"));
        dto.setSaas("slack");
        dto.setChannelId((String) event.get("channel_id"));
        return dto;
    }

    private SlackUserJoinedEventDto convertToUserJoinedEventDto(Map<String, Object> payload, String org_webhook_url) {
        Map<String, Object> event = (Map<String, Object>) payload.get("event");
        Map<String, Object> user = (Map<String, Object>) event.get("user");
        SlackUserJoinedEventDto dto = new SlackUserJoinedEventDto();
        dto.setFrom(org_webhook_url);
        dto.setEvent((String) event.get("type"));
        dto.setSaas("slack");
        dto.setJoinedUserId((String) user.get("id"));
        return dto;
    }
}
