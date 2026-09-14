package com.cursedshrine.apinexus.notification.adapter;

import com.cursedshrine.apinexus.notification.entity.NotificationEntity;
import com.cursedshrine.apinexus.notification.proxy.NotificationProxy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class NotificationAdapter {

    private final WebClient client;

    public NotificationAdapter(@Qualifier("mailWebClient") WebClient client) {
        this.client = client;
    }

    public Map<String, Object> subscribe(NotificationEntity entity) {
        NotificationProxy proxy = NotificationProxy.builder()
                .name(entity.getSubscriberName())
                .email(entity.getSubscriberEmail())
                .build();

        // Mail-Service replies 201 with an empty body, so there is nothing to map.
        client.post()
                .uri("/save/subscriber")
                .header("clientId", "api-nexus")
                .bodyValue(proxy)
                .retrieve()
                .toBodilessEntity()
                .block();
        return Map.of();
    }
}
