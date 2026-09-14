package com.cursedshrine.apinexus.notification.controller;

import com.cursedshrine.apinexus.notification.dto.NotificationRequestDto;
import com.cursedshrine.apinexus.notification.dto.NotificationResponseDto;
import com.cursedshrine.apinexus.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    // Deliberately loose: something@something.tld. Real validation is the confirmation mail.
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private final NotificationService service;

    // Was POST /notification/send with {to, from, subject, body}. Mail-Service has no
    // send capability — only /save/subscriber {name, email} — so every call 422'd.
    @PostMapping("/subscribe")
    public ResponseEntity<NotificationResponseDto> subscribe(@RequestBody NotificationRequestDto dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            return ResponseEntity.badRequest().body(new NotificationResponseDto(false, "name required"));
        }
        if (dto.getEmail() == null || !EMAIL.matcher(dto.getEmail().trim()).matches()) {
            return ResponseEntity.badRequest().body(new NotificationResponseDto(false, "valid email required"));
        }
        service.subscribe(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new NotificationResponseDto(true, "subscriber saved"));
    }
}
