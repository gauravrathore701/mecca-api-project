package com.cursedshrine.apinexus.auth.proxy;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

// Shape of the request body sent to the downstream auth API
@Data
@Builder
public class RegisterProxy {
    private String username;
    private String email;
    private String password;
    private Map<String, Object> extras;
}
