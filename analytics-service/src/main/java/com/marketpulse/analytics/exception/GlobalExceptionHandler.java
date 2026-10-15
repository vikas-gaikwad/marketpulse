
package com.marketpulse.analytics.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            IllegalArgumentException exception) {

        Map<String, Object> error = new LinkedHashMap<>();
        error.put("error", "Invalid request");
        error.put("message", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
    @ExceptionHandler(MarketDataServiceUnavailableException.class)
    public ResponseEntity<Map<String, Object>> handleMarketDataServiceUnavailable(
            MarketDataServiceUnavailableException exception) {

        Map<String, Object> error = new LinkedHashMap<>();
        error.put("error", "Market Data Service unavailable");
        error.put("message", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(error);
    }
}
