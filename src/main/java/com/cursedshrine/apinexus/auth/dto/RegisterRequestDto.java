package com.cursedshrine.apinexus.auth.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class RegisterRequestDto {
    private String username;
    private String email;
    private String password;

    // captures any extra fields sent by the caller
    private Map<String, Object> extras = new HashMap<>();

    @JsonAnySetter
    public void addExtra(String key, Object value) {
        extras.put(key, value);
    }
}
