package com.cursedshrine.apinexus.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<Map<String, Object>> handleDownstreamError(WebClientResponseException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(Map.of(
                "success", false,
                "message", "downstream error: " + ex.getMessage(),
                "status", ex.getStatusCode().value()
        ));
    }

    // A protected endpoint called without an Authorization header is an auth
    // failure, not a server fault — otherwise this falls through to the 500 below.
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Map<String, Object>> handleMissingHeader(MissingRequestHeaderException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "success", false,
                "message", "missing required header: " + ex.getHeaderName(),
                "status", HttpStatus.UNAUTHORIZED.value()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", ex.getMessage()
        ));
    }
}
