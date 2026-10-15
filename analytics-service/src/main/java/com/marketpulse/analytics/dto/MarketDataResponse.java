package com.marketpulse.analytics.dto;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public record MarketDataResponse(
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
