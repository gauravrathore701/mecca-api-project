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

    public Map<String, Object> send(NotificationEntity entity) {
        NotificationProxy proxy = NotificationProxy.builder()
                .to(entity.getRecipient())
                .from(entity.getSender())
                .subject(entity.getSubject())
                .body(entity.getContent())
                .build();

        return client.post()
                .uri("/save/subscriber")
                .header("clientId", "api-nexus")
                .bodyValue(proxy)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}
