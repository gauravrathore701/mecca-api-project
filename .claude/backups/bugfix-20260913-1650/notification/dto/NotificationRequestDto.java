package com.cursedshrine.apinexus.notification.dto;

import lombok.Data;

@Data
public class NotificationRequestDto {
    private String to;
    private String subject;
    private String body;
    private String from;
}
