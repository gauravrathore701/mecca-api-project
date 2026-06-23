package com.cursedshrine.apinexus.auth.proxy;

import lombok.Builder;
import lombok.Data;

// Shape of the request body sent to the downstream auth API
@Data
@Builder
public class LoginProxy {
    private String username;
    private String password;
}
