
package com.marketpulse.analytics.controller;

import com.marketpulse.analytics.service.AnalyticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/{symbol}/ema")
    public Map<String, Object> calculateEma(
            @PathVariable String symbol,
            @RequestParam int period,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to) {

        BigDecimal ema = analyticsService.calculateEma(
                symbol, period, from, to);

        return Map.of(
                "symbol", symbol.toUpperCase(),
                "indicator", "EMA",
                "period", period,
                "from", from,
                "to", to,
                "value", ema
        );
    }
    @GetMapping("/{symbol}/rsi")
    public ResponseEntity<Map<String, Object>> calculateRsi(
            @PathVariable String symbol,
            @RequestParam int period,
            @RequestParam LocalDate from,
            @RequestParam LocalDate to) {

        BigDecimal rsi = analyticsService.calculateRsi(
                symbol, period, from, to);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("symbol", symbol.toUpperCase(Locale.ROOT));
        response.put("indicator", "RSI");
        response.put("period", period);
        response.put("from", from);
        response.put("to", to);
        response.put("value", rsi);

        return ResponseEntity.ok(response);
    }
}
