package com.marketpulse.analytics.controller;


import com.marketpulse.analytics.client.MarketDataClient;
import com.marketpulse.analytics.dto.MarketDataResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class MarketDataIntegrationController {

    private final MarketDataClient marketDataClient;

    public MarketDataIntegrationController(
            MarketDataClient marketDataClient) {
        this.marketDataClient = marketDataClient;
    }

    @GetMapping("/{symbol}/market-data")
    public List<MarketDataResponse> getMarketData(
            @PathVariable String symbol,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to) {

        return marketDataClient.getHistoricalData(symbol, from, to);
    }
}
