package com.grummang.webhook_server.service.GoogleDrive;

import com.grummang.webhook_server.model.dto.google_drive.GoogleDriveChangeEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class GoogleDriveEventHandlerImpl implements GoogleDriveEventHandler {

    @Value("${event.processing.server.url.google-drive}")
    private String eventProcessingServerUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public GoogleDriveEventHandlerImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void handleChangeEvent(GoogleDriveChangeEventDto changeEventDto) {
        String url = eventProcessingServerUrl + "/file-change";
        try {
            restTemplate.postForEntity(url, changeEventDto, String.class);
            log.info("Handling Google Drive change event: {}", changeEventDto);
        } catch (RestClientException e) {
            log.error("Failed to send Google Drive change event to {}: {}", url, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error while handling Google Drive change event: {}", e.getMessage(), e);
        }
    }
}