package com.cursedshrine.apinexus.auth.manager;

import com.cursedshrine.apinexus.auth.adapter.AuthAdapter;
import com.cursedshrine.apinexus.auth.entity.LoginEntity;
import com.cursedshrine.apinexus.auth.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthManager {

    private final AuthAdapter adapter;

    public Map<String, Object> register(UserEntity entity) {
        return adapter.register(entity);
    }

    public Map<String, Object> login(LoginEntity entity) {
        return adapter.login(entity);
    }
}
