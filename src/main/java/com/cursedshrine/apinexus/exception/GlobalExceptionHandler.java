package com.cursedshrine.apinexus.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;

/**
 * Public error responses must never carry internal details (downstream hosts,
 * ports, paths, stack messages). Full detail goes to the log only.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Downstream answered with 4xx/5xx. Pass the status through, and the downstream's
    // own short "error"/"message" text if it sent JSON — never ex.getMessage(), which
    // embeds the internal URL (e.g. "from POST http://localhost:4183/users/login").
    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<Map<String, Object>> handleDownstreamError(WebClientResponseException ex) {
        log.warn("downstream error: {}", ex.getMessage());
        HttpStatusCode status = ex.getStatusCode();
        return body(status, downstreamText(ex, status));
    }

    // Downstream unreachable (connection refused, timeout, DNS).
    @ExceptionHandler(WebClientRequestException.class)
    public ResponseEntity<Map<String, Object>> handleDownstreamUnreachable(WebClientRequestException ex) {
        log.error("downstream unreachable: {}", ex.getMessage());
        return body(HttpStatus.BAD_GATEWAY, "upstream service unavailable");
    }

    // A protected endpoint called without an Authorization header is an auth
    // failure, not a server fault — otherwise this falls through to the 500 below.
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Map<String, Object>> handleMissingHeader(MissingRequestHeaderException ex) {
        return body(HttpStatus.UNAUTHORIZED, "missing required header: " + ex.getHeaderName());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleBadJson(HttpMessageNotReadableException ex) {
        return body(HttpStatus.BAD_REQUEST, "malformed JSON body");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoResourceFoundException ex) {
        return body(HttpStatus.NOT_FOUND, "not found");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleMethod(HttpRequestMethodNotSupportedException ex) {
        return body(HttpStatus.METHOD_NOT_ALLOWED, "method not allowed");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        log.error("unhandled error", ex);
        return body(HttpStatus.INTERNAL_SERVER_ERROR, "internal error");
    }

    private static ResponseEntity<Map<String, Object>> body(HttpStatusCode status, String message) {
        return ResponseEntity.status(status).body(Map.of(
                "success", false,
                "message", message,
                "status", status.value()
        ));
    }

    @SuppressWarnings("unchecked")
    private static String downstreamText(WebClientResponseException ex, HttpStatusCode status) {
        try {
            Map<String, Object> m = ex.getResponseBodyAs(Map.class);
            if (m != null) {
                for (String key : new String[]{"error", "message"}) {
                    Object v = m.get(key);
                    if (v instanceof String s && !s.isBlank() && s.length() <= 200) return s;
                }
            }
        } catch (Exception ignored) {
            // non-JSON body (e.g. plain text) — fall back to the reason phrase
        }
        HttpStatus known = HttpStatus.resolve(status.value());
        return known != null ? known.getReasonPhrase().toLowerCase() : "request failed";
    }
}
