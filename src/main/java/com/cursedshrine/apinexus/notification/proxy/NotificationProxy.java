package com.cursedshrine.apinexus.notification.proxy;

import lombok.Builder;
import lombok.Data;

// Shape of the request body sent to the downstream mail API (POST /save/subscriber).
// Must match Mail-Service's Subscriber model: { name, email }.
@Data
@Builder
public class NotificationProxy {
    private String name;
    private String email;
}
