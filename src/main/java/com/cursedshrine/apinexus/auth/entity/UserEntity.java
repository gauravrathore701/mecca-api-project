package com.cursedshrine.apinexus.auth.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class UserEntity {
    private String username;
    private String email;
    private String password;
    private Map<String, Object> extras;
}
