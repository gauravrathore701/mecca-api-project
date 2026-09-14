package com.cursedshrine.apinexus.notification.dto;

import lombok.Data;

// Mail-Service can only store subscribers, so this is the whole contract.
@Data
public class NotificationRequestDto {
    private String name;
    private String email;
}
