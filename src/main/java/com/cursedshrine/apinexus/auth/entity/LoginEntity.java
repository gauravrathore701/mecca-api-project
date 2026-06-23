package com.cursedshrine.apinexus.auth.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginEntity {
    private String username;
    private String password;
}
