package com.cursedshrine.apinexus.notification.manager;

import com.cursedshrine.apinexus.notification.adapter.NotificationAdapter;
import com.cursedshrine.apinexus.notification.entity.NotificationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationManager {

    private final NotificationAdapter adapter;

    public Map<String, Object> subscribe(NotificationEntity entity) {
        return adapter.subscribe(entity);
    }
}
