package com.grummang.webhook_server.controller;

import com.grummang.webhook_server.dto.slack.*;
import com.grummang.webhook_server.service.GoogleDrive.GoogleDriveService;
import com.grummang.webhook_server.service.Slack.SlackDtoFunc;
import com.grummang.webhook_server.service.Slack.SlackEventDistributor;
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
    // private final GoogleDriveService googleDriveService;
    private final SlackDtoFunc slackDtoFunc;

    @Autowired
    public WebhookController(SlackEventDistributor slackEventDistributor,
                             SlackDtoFunc slackDtoFunc) {
        this.slackEventDistributor = slackEventDistributor;
        // this.googleDriveService = googleDriveService;
        this.slackDtoFunc = slackDtoFunc;
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

            Map<String, Object> eventMap = slackDtoFunc.castToMap(payload.get("event"));
            String eventType = (String) eventMap.get("type");
            String teamId = (String) payload.get("team_id");
            switch (eventType) {
                case "file_shared" -> {
                    SlackFileSharedEventDto fileSharedEventDto = slackDtoFunc.convertToFileSharedEventDto(eventMap, teamId, org_webhook_url);
                    log.info("File shared event: {}", fileSharedEventDto);
                    slackEventDistributor.distributeEvent(fileSharedEventDto);
                }
                case "member_joined_channel" -> {
                    SlackMemberJoinedChannelEventDto memberJoinedChannelEventDto = slackDtoFunc.convertToMemberJoinedChannelEventDto(eventMap, teamId,org_webhook_url);
                    slackEventDistributor.distributeEvent(memberJoinedChannelEventDto);
                }
                case "channel_created" -> {
                    SlackChannelCreatedEventDto channelCreatedEventDto = slackDtoFunc.convertToChannelCreatedEventDto(eventMap, teamId,org_webhook_url);
                    slackEventDistributor.distributeEvent(channelCreatedEventDto);
                }
                case "team_join" -> {
                    SlackUserJoinedEventDto userJoinedEventDto = slackDtoFunc.convertToUserJoinedEventDto(eventMap, teamId, org_webhook_url);
                    slackEventDistributor.distributeEvent(userJoinedEventDto);
                }
                case "file_change" ->{
                    SlackFileChangeEventDto fileChangeEventDto = slackDtoFunc.convertToFileChangeEventDto(eventMap, teamId, org_webhook_url);
                    slackEventDistributor.distributeEvent(fileChangeEventDto);
                }
                case "file_deleted" -> {
                    SlackFileDeletedEventDto fileDeletedEventDto = slackDtoFunc.convertToFileDeletedEventDto(eventMap, teamId, org_webhook_url);
                    slackEventDistributor.distributeEvent(fileDeletedEventDto);
                }
                default -> log.warn("Unsupported event type: {}", eventType);
            }

            return ResponseEntity.ok("Event received successfully");
        } catch (Exception e) {
            log.error("Error processing event", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing event");
        }
    }
    // @PostMapping("/google-drive/{org_webhook_url}")
    // public ResponseEntity<String> handleEvent(@RequestParam int tenant_id, @RequestBody Map<String, Object> payload) {
    //     log.info("Received Google Drive Event: {}", payload);

    //     // 이벤트 타입에 따른 처리 로직
    //     String eventType = (String) payload.get("eventType");
    //     switch (eventType) {
    //         case "change" -> googleDriveService.handleFileChangeEvent(payload);
    //         case "delete" -> googleDriveService.handleFileDeleteEvent(payload);

    //         // 추가 이벤트 타입 처리 가능
    //         default -> log.warn("Unhandled event type: {}", eventType);
    //     }
    //     return ResponseEntity.ok("Google Drive Event received and logged");
    // }

}
