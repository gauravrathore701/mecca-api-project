package com.cursedshrine.apinexus.auth.controller;

import com.cursedshrine.apinexus.auth.dto.AuthResponseDto;
import com.cursedshrine.apinexus.auth.dto.LoginRequestDto;
import com.cursedshrine.apinexus.auth.dto.RegisterRequestDto;
import com.cursedshrine.apinexus.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterRequestDto dto) {
        Map<String, Object> result = service.register(dto);
        return ResponseEntity.ok(new AuthResponseDto(true, "registered", result));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto dto) {
        Map<String, Object> result = service.login(dto);
        return ResponseEntity.ok(new AuthResponseDto(true, "authenticated", result));
    }
}
