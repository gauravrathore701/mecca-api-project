package com.cursedshrine.apinexus.auth.service;

import com.cursedshrine.apinexus.auth.dto.LoginRequestDto;
import com.cursedshrine.apinexus.auth.dto.RegisterRequestDto;
import com.cursedshrine.apinexus.auth.entity.LoginEntity;
import com.cursedshrine.apinexus.auth.entity.UserEntity;
import com.cursedshrine.apinexus.auth.manager.AuthManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthManager manager;

    public Map<String, Object> register(RegisterRequestDto dto) {
        UserEntity entity = UserEntity.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .extras(dto.getExtras())
                .build();
        return manager.register(entity);
    }

    public Map<String, Object> login(LoginRequestDto dto) {
        LoginEntity entity = LoginEntity.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .build();
        return manager.login(entity);
    }
}
