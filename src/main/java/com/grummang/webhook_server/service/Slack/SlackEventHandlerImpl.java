package com.grummang.webhook_server.service.Slack;

import com.grummang.webhook_server.dto.slack.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Slf4j
public class SlackEventHandlerImpl implements SlackEventHandler {

    @Value("${event.processing.server.url}")
    private String eventProcessingServerUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public SlackEventHandlerImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public String handleEventSubscribed(Map<String, Object> payload) {
        return (String) payload.get("challenge");
    }


    @Override
    public void handleFileSharedEvent(SlackFileSharedEventDto eventDto) {
        // 파일 공유 이벤트 처리 로직
        String url = eventProcessingServerUrl + "/file-shared";
        try {
            restTemplate.postForEntity(url, eventDto, String.class);
            log.info("Handling file shared event: {}", eventDto);
        } catch (RestClientException e) {
            log.error("Failed to send file shared event to {}: {}", url, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error while handling file shared event: {}", e.getMessage(), e);
        }
    }

    @Override
    public void handleMemberJoinedChannelEvent(SlackMemberJoinedChannelEventDto eventDto) {
        // 멤버 채널 참여 이벤트 처리 로직
        String url = eventProcessingServerUrl + "/member-joined-channel";
        try {
            restTemplate.postForEntity(url, eventDto, String.class);
            log.info("Handling member joined channel event: {}", eventDto);
        } catch (RestClientException e) {
            log.error("Failed to send member joined channel event to {}: {}", url, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error while handling member joined channel event: {}", e.getMessage(), e);
        }
    }

    @Override
    public void handleChannelCreatedEvent(SlackChannelCreatedEventDto eventDto) {
        // 채널 생성 이벤트 처리 로직
        String url = eventProcessingServerUrl + "/channel-created";
        try {
            restTemplate.postForEntity(url, eventDto, String.class);
            log.info("Handling channel created event: {}", eventDto);
        } catch (RestClientException e) {
            log.error("Failed to send channel created event to {}: {}", url, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error while handling channel created event: {}", e.getMessage(), e);
        }
    }


    @Override
    public void handleUserJoinedEvent(SlackUserJoinedEventDto userJoinedEventDto) {
        // 사용자 가입 이벤트 처리 로직
        String url = eventProcessingServerUrl + "/user-joined";
        try {
            restTemplate.postForEntity(url, userJoinedEventDto, String.class);
            log.info("Handling user joined event: {}" , userJoinedEventDto);
        } catch (RestClientException e) {
            log.error("Failed to send user joined event to {}: {}", url, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error while handling user joined event: {}", e.getMessage(), e);
        }
    }

    @Override
    public void handleFileChangedEvent(SlackFileChangeEventDto slackFileChangeEventDto) {
        // 파일 변경 이벤트 처리 로직
        String url = eventProcessingServerUrl + "/file-change";
        try {
            restTemplate.postForEntity(url, slackFileChangeEventDto, String.class);
            log.info("Handling file changed event: {}", slackFileChangeEventDto);
        } catch (RestClientException e) {
            log.error("Failed to send file changed event to {}: {}", url, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error while handling file changed event: {}", e.getMessage(), e);
        }
    }

    @Override
    public void handleFileDeletedEvent(SlackFileDeletedEventDto slackFileDeletedEventDto) {
        // 파일 삭제 이벤트 처리 로직
        String url = eventProcessingServerUrl + "/file-delete";
        try {
            restTemplate.postForEntity(url, slackFileDeletedEventDto, String.class);
            log.info("Handling file deleted event: {}", slackFileDeletedEventDto);
        } catch (RestClientException e) {
            log.error("Failed to send file deleted event to {}: {}", url, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error while handling file deleted event: {}", e.getMessage(), e);
        }
    }
}
