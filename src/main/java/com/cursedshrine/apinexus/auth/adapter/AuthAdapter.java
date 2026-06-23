package com.cursedshrine.apinexus.auth.adapter;

import com.cursedshrine.apinexus.auth.entity.LoginEntity;
import com.cursedshrine.apinexus.auth.entity.UserEntity;
import com.cursedshrine.apinexus.auth.proxy.LoginProxy;
import com.cursedshrine.apinexus.auth.proxy.RegisterProxy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class AuthAdapter {

    private final WebClient client;

    public AuthAdapter(@Qualifier("authWebClient") WebClient client) {
        this.client = client;
    }

    public Map<String, Object> register(UserEntity entity) {
        RegisterProxy proxy = RegisterProxy.builder()
                .username(entity.getUsername())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .extras(entity.getExtras())
                .build();

        return client.post()
                .uri("/users/register")
                .bodyValue(proxy)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public Map<String, Object> login(LoginEntity entity) {
        LoginProxy proxy = LoginProxy.builder()
                .username(entity.getUsername())
                .password(entity.getPassword())
                .build();

        return client.post()
                .uri("/users/login")
                .bodyValue(proxy)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}
