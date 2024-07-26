package com.grummang.webhook_server.controller;

import com.grummang.webhook_server.dto.SlackChannelCreatedEventDto;
import com.grummang.webhook_server.dto.SlackFileSharedEventDto;
import com.grummang.webhook_server.dto.SlackMemberJoinedChannelEventDto;
import com.grummang.webhook_server.dto.SlackUserJoinedEventDto;
import com.grummang.webhook_server.service.SlackEventDistributor;
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
    public ResponseEntity<String> handleSlackEvent(@Valid @RequestBody Map<String, Object> payload, @PathVariable String org_webhook_url) {
        log.info("Received Slack event: {}", payload);

        System.out.println(org_webhook_url);
        try {
            String type = (String) payload.get("type");
            if ("url_verification".equals(type)) {
                String challenge = (String) payload.get("challenge");
                return ResponseEntity.ok(challenge);
            }

            Map<String, Object> eventMap = castToMap(payload.get("event"));
            String eventType = (String) eventMap.get("type");
            String teamId = (String) payload.get("team_id");
            switch (eventType) {
                case "file_shared" -> {
                    SlackFileSharedEventDto fileSharedEventDto = convertToFileSharedEventDto(eventMap, teamId, org_webhook_url);
                    log.info("File shared event: {}", fileSharedEventDto);
                    slackEventDistributor.distributeEvent(fileSharedEventDto);
                }
                case "member_joined_channel" -> {
                    SlackMemberJoinedChannelEventDto memberJoinedChannelEventDto = convertToMemberJoinedChannelEventDto(eventMap, teamId,org_webhook_url);
                    slackEventDistributor.distributeEvent(memberJoinedChannelEventDto);
                }
                case "channel_created" -> {
                    SlackChannelCreatedEventDto channelCreatedEventDto = convertToChannelCreatedEventDto(eventMap, teamId,org_webhook_url);
                    slackEventDistributor.distributeEvent(channelCreatedEventDto);
                }
                case "team_join" -> {
                    SlackUserJoinedEventDto userJoinedEventDto = convertToUserJoinedEventDto(eventMap, teamId, org_webhook_url);
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

    private SlackFileSharedEventDto convertToFileSharedEventDto(Map<String, Object> eventMap,String teamId, String org_webhook_url) {
        SlackFileSharedEventDto dto = new SlackFileSharedEventDto();
        dto.setFrom(org_webhook_url);
        dto.setEvent((String) eventMap.get("type"));
        dto.setSaas("slack");
        dto.setTeamId(teamId);
        dto.setFileId((String) eventMap.get("file_id"));
        return dto;
    }

    private SlackMemberJoinedChannelEventDto convertToMemberJoinedChannelEventDto(Map<String, Object> eventMap, String teamId,String org_webhook_url) {
        SlackMemberJoinedChannelEventDto dto = new SlackMemberJoinedChannelEventDto();
        dto.setFrom(org_webhook_url);
        dto.setEvent((String) eventMap.get("type"));
        dto.setToken((String) eventMap.get("token"));
        dto.setTeamId(teamId);
        dto.setApiAppId((String) eventMap.get("api_app_id"));
        dto.setJoinedUser((String) eventMap.get("user"));
        dto.setJoinedChannel((String) eventMap.get("channel"));
        dto.setTimestamp((String) eventMap.get("event_ts"));
        return dto;
    }

    private SlackChannelCreatedEventDto convertToChannelCreatedEventDto(Map<String, Object> eventMap, String teamId,String org_webhook_url) {
        SlackChannelCreatedEventDto dto = new SlackChannelCreatedEventDto();
        dto.setFrom(org_webhook_url);
        dto.setEvent((String) eventMap.get("type"));
        dto.setSaas("slack");
        dto.setTeamId(teamId);
        dto.setChannelId((String) eventMap.get("channel_id"));
        return dto;
    }

    private SlackUserJoinedEventDto convertToUserJoinedEventDto(Map<String, Object> eventMap, String teamId, String org_webhook_url) {
        Map<String, Object> user = castToMap(eventMap.get("user"));
        SlackUserJoinedEventDto dto = new SlackUserJoinedEventDto();
        dto.setFrom(org_webhook_url);
        dto.setEvent((String) eventMap.get("type"));
        dto.setSaas("slack");
        dto.setTeamId(teamId);
        dto.setJoinedUserId((String) user.get("id"));
        return dto;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> castToMap(Object object) {
        return (Map<String, Object>) object;
    }
}
