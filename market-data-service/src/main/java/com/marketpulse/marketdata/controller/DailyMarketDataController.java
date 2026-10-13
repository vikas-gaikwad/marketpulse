package com.marketpulse.marketdata.controller;

import com.marketpulse.marketdata.dto.DailyMarketDataRequest;
import com.marketpulse.marketdata.dto.DailyMarketDataResponse;
import com.marketpulse.marketdata.entity.DailyMarketData;
import com.marketpulse.marketdata.service.DailyMarketDataService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/market-data")
public class DailyMarketDataController {

    private final DailyMarketDataService service;

    public DailyMarketDataController(DailyMarketDataService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DailyMarketDataResponse create(
            @Valid @RequestBody DailyMarketDataRequest request) {

        DailyMarketData saved = service.create(toEntity(request));
        return toResponse(saved);
    }

    @GetMapping("/{symbol}/latest")
    public DailyMarketDataResponse getLatest(
            @PathVariable String symbol) {

        return toResponse(service.getLatest(symbol));
    }

    @GetMapping("/{symbol}/history")
    public List<DailyMarketDataResponse> getHistory(
            @PathVariable String symbol,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to) {

        return service.getHistory(symbol, from, to)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private DailyMarketData toEntity(DailyMarketDataRequest request) {
        DailyMarketData data = new DailyMarketData();
        data.setSymbol(request.symbol());
        data.setTradingDate(request.tradingDate());
        data.setOpenPrice(request.openPrice());
        data.setHighPrice(request.highPrice());
        data.setLowPrice(request.lowPrice());
        data.setClosePrice(request.closePrice());
        data.setVolume(request.volume());
        return data;
    }

    private DailyMarketDataResponse toResponse(DailyMarketData data) {
        return new DailyMarketDataResponse(
                data.getId(),
                data.getSymbol(),
                data.getTradingDate(),
                data.getOpenPrice(),
                data.getHighPrice(),
                data.getLowPrice(),
                data.getClosePrice(),
                data.getVolume(),
                data.getCreatedAt()
        );
    }
}

