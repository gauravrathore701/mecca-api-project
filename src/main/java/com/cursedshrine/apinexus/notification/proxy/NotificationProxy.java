package com.cursedshrine.apinexus.notification.proxy;

import lombok.Builder;
import lombok.Data;

// Shape of the request body sent to the downstream mail API
@Data
@Builder
public class NotificationProxy {
    private String to;
    private String from;
    private String subject;
    private String body;
}
