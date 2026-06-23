package com.cursedshrine.apinexus.notification.service;

import com.cursedshrine.apinexus.notification.dto.NotificationRequestDto;
import com.cursedshrine.apinexus.notification.entity.NotificationEntity;
import com.cursedshrine.apinexus.notification.manager.NotificationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationManager manager;

    public Map<String, Object> send(NotificationRequestDto dto) {
        NotificationEntity entity = NotificationEntity.builder()
                .recipient(dto.getTo())
                .sender(dto.getFrom())
                .subject(dto.getSubject())
                .content(dto.getBody())
                .build();
        return manager.send(entity);
    }
}
