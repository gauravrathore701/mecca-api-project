package com.cursedshrine.apinexus.progress.adapter;

import com.cursedshrine.apinexus.progress.entity.ProgressEntity;
import com.cursedshrine.apinexus.progress.proxy.ProgressProxy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class ProgressAdapter {

    private final WebClient client;

    public ProgressAdapter(@Qualifier("authWebClient") WebClient client) {
        this.client = client;
    }

    // The downstream auth API owns the DB and verifies the JWT itself, so the
    // caller's Authorization header is forwarded untouched — nothing is decoded here.
    public Map<String, Object> save(ProgressEntity entity, String authorization) {
        ProgressProxy proxy = ProgressProxy.builder()
                .show(entity.getShow())
                .path(entity.getPath())
                .position(entity.getPosition())
                .duration(entity.getDuration())
                .finished(entity.getFinished())
                .build();

        return client.post()
                .uri("/progress")
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .bodyValue(proxy)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public Map<String, Object> list(String show, String authorization) {
        return client.get()
                .uri(builder -> {
                    builder.path("/progress");
                    if (show != null && !show.isBlank()) {
                        builder.queryParam("show", show);
                    }
                    return builder.build();
                })
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}
