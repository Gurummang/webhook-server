package com.grummang.webhook_server.service.O365;

import com.grummang.webhook_server.model.dto.o365.OneDriveFileChangeEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class O365DtoFunc {

    public OneDriveFileChangeEventDto convertToOneDriveFileChangeEventDto(String userId, String tenantId, String changeType) {
        OneDriveFileChangeEventDto dto = new OneDriveFileChangeEventDto();
        dto.setUserId(userId);
        dto.setTenantId(tenantId);
        dto.setChageType(changeType);
        return dto;
    }

}
