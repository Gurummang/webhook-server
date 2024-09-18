package com.grummang.webhook_server.service.O365;

import com.grummang.webhook_server.model.dto.o365.OneDriveFileChangeEventDto;
import com.grummang.webhook_server.model.dto.slack.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Slf4j
public class O365EventHandlerImpl implements O365EventHandler {

    @Value("${event.processing.server.url.o365}")
    private String eventProcessingServerUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public O365EventHandlerImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void handleFileChangedEvent(OneDriveFileChangeEventDto oneDriveFileChangeEventDto) {
        // 파일 변경 이벤트 처리 로직
        String url = eventProcessingServerUrl + "/file-change";
        try {
            restTemplate.postForEntity(url, oneDriveFileChangeEventDto, String.class);
            log.info("Handling file changed event: {}", oneDriveFileChangeEventDto);
        } catch (RestClientException e) {
            log.error("Failed to send file changed event to {}: {}", url, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error while handling file changed event: {}", e.getMessage(), e);
        }
    }
}
