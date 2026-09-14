package com.cursedshrine.apinexus.notification.controller;

import com.cursedshrine.apinexus.notification.dto.NotificationRequestDto;
import com.cursedshrine.apinexus.notification.dto.NotificationResponseDto;
import com.cursedshrine.apinexus.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @PostMapping("/send")
    public ResponseEntity<NotificationResponseDto> send(@RequestBody NotificationRequestDto dto) {
        service.send(dto);
        return ResponseEntity.ok(new NotificationResponseDto(true, "notification dispatched"));
    }
}
