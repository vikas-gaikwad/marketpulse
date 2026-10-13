package com.marketpulse.marketdata.dto;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public record DailyMarketDataResponse(
        Long id,
        String symbol,
        LocalDate tradingDate,
        BigDecimal openPrice,
        BigDecimal highPrice,
        BigDecimal lowPrice,
        BigDecimal closePrice,
        Long volume,
        OffsetDateTime createdAt
) {
}

