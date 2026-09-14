package com.cursedshrine.apinexus.notification.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationEntity {
    private String subscriberName;
    private String subscriberEmail;
}
