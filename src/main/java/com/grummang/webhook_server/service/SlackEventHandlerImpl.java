package com.grummang.webhook_server.service;

import com.grummang.webhook_server.dto.SlackChannelCreatedEventDto;
import com.grummang.webhook_server.dto.SlackFileSharedEventDto;
import com.grummang.webhook_server.dto.SlackMemberJoinedChannelEventDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class SlackEventHandlerImpl implements SlackEventHandler {

    @Value("${event.processing.server.url}")
    private String eventProcessingServerUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void handleFileSharedEvent(SlackFileSharedEventDto eventDto) {
        // 파일 공유 이벤트 처리 로직
        String url = eventProcessingServerUrl + "/file-shared";
        restTemplate.postForEntity(url, eventDto, String.class);
        System.out.println("Handling file shared event: " + eventDto);
    }

    @Override
    public void handleMemberJoinedChannelEvent(SlackMemberJoinedChannelEventDto eventDto) {
        // 멤버 채널 참여 이벤트 처리 로직
        String url = eventProcessingServerUrl + "/member-joined-channel";
        restTemplate.postForEntity(url, eventDto, String.class);
        System.out.println("Handling member joined channel event: " + eventDto);
    }

    @Override
    public void handleChannelCreatedEvent(SlackChannelCreatedEventDto eventDto) {
        // 채널 생성 이벤트 처리 로직
        String url = eventProcessingServerUrl + "/channel-created";
        restTemplate.postForEntity(url, eventDto, String.class);
        System.out.println("Handling channel created event: " + eventDto);
    }

    @Override
    public String handleEventSubscribed(Map<String, Object> payload) {
        return (String) payload.get("challenge");
    }
}
