package com.grummang.webhook_server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grummang.webhook_server.model.dto.o365.OneDriveFileChangeEventDto;
import com.grummang.webhook_server.model.dto.slack.*;
import com.grummang.webhook_server.service.O365.O365DtoFunc;
import com.grummang.webhook_server.service.O365.O365EventDistributor;
import com.grummang.webhook_server.service.Slack.SlackDtoFunc;
import com.grummang.webhook_server.service.Slack.SlackEventDistributor;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/webhook")
@Slf4j
public class WebhookController {

    private final SlackEventDistributor slackEventDistributor;
    private final O365EventDistributor o365EventDistributor;
    private final SlackDtoFunc slackDtoFunc;
    private final O365DtoFunc o365DtoFunc;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public WebhookController(SlackEventDistributor slackEventDistributor,O365EventDistributor o365EventDistributor,
                             SlackDtoFunc slackDtoFunc, O365DtoFunc o365DtoFunc) {
        this.slackEventDistributor = slackEventDistributor;
        this.o365EventDistributor = o365EventDistributor;
        this.slackDtoFunc = slackDtoFunc;
        this.o365DtoFunc = o365DtoFunc;

    }

    @PostMapping("/slack/{org_webhook_url}")
    public ResponseEntity<String> handleSlackEvent(@Valid @RequestBody Map<String, Object> payload, @PathVariable String org_webhook_url) {
        log.info("Received Slack event: {}", payload);

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

    @PostMapping("/o365/{tenantId}")
    public ResponseEntity<String> handleO365Event(@PathVariable String tenantId,
                                                  @RequestParam(value = "validationToken", required = false) String validationToken,
                                                  @RequestHeader Map<String, String> headers,
                                                  @RequestBody(required = false) String payload) {
        try {
            log.info("Received Microsoft O365 Event for tenant {}: {}", "tenantId", payload);
            log.info("Headers: {}", headers);
            log.info("tenantId: {}", tenantId);


            // 1. Validation-Token이 URL의 쿼리 파라미터로 전달된 경우 처리
            if (validationToken != null) {
                log.info("Validation-Token received: {}", validationToken);
                return ResponseEntity.ok(validationToken);
            }

            Map<String, Object> payloadMap = castToMap(payload);
            log.info("Payload: {}", payloadMap);
            for(Map<String, Object> value : (Iterable<Map<String, Object>>) payloadMap.get("value")) {
                String user_id = value.get("resource").toString();
                String tenant_id = value.get("tenantId").toString();
                String changeType = value.get("changeType").toString();
                switch (changeType){
                    case "updated" -> {
                        OneDriveFileChangeEventDto oneDriveFileChangeEventDto = o365DtoFunc.convertToOneDriveFileChangeEventDto(user_id, tenant_id, changeType);
                        log.info("OneDrive File Change Event: {}", oneDriveFileChangeEventDto);
                        o365EventDistributor.distributeEvent(oneDriveFileChangeEventDto);
                    }
                    case "deleted" -> {
                        // 삭제 이벤트 처리 로직
                    }
                    case "created" -> {
                        // 생성 이벤트 처리 로직
                    }
                    default -> log.warn("Unsupported event type: {}", changeType);
                }
            }

            // 2. 요청 바디가 비어 있는 경우에 대한 처리
            if (payload == null || payload.isEmpty()) {
                log.warn("Received request with empty or missing body for tenant {}", "tenantId");
                return ResponseEntity.badRequest().body("Request received without a body for tenant " + "tenantId");
            }



            // 3. 실제 이벤트 처리 로직
            log.info("Processing event for tenant: {}", "tenantId");

            return ResponseEntity.ok("O365 Event received and processed for tenant " + "tenantId");
        } catch (Exception e) {
            log.error("Error processing event", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing event");
        }
    }

    private Map<String,Object> castToMap(String payload){
        try {
            return objectMapper.readValue(payload, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON payload", e);
        }
    }
}
