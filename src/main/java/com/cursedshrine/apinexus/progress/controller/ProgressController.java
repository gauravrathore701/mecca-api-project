package com.cursedshrine.apinexus.progress.controller;

import com.cursedshrine.apinexus.progress.dto.ProgressRequestDto;
import com.cursedshrine.apinexus.progress.dto.ProgressResponseDto;
import com.cursedshrine.apinexus.progress.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService service;

    @PostMapping
    public ResponseEntity<ProgressResponseDto> save(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @RequestBody ProgressRequestDto dto) {
        Map<String, Object> result = service.save(dto, authorization);
        return ResponseEntity.ok(new ProgressResponseDto(true, "saved", result));
    }

    @GetMapping
    public ResponseEntity<ProgressResponseDto> list(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @RequestParam(required = false) String show) {
        Map<String, Object> result = service.list(show, authorization);
        return ResponseEntity.ok(new ProgressResponseDto(true, "ok", result));
    }
}
