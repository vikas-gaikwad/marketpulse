package com.marketpulse.marketdata.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailyMarketDataRequest(

        @NotBlank(message = "Symbol is required")
        String symbol,

        @NotNull(message = "Trading date is required")
        LocalDate tradingDate,

        @NotNull(message = "Open price is required")
        @Positive(message = "Open price must be positive")
        BigDecimal openPrice,

        @NotNull(message = "High price is required")
        @Positive(message = "High price must be positive")
        BigDecimal highPrice,

        @NotNull(message = "Low price is required")
        @Positive(message = "Low price must be positive")
        BigDecimal lowPrice,

        @NotNull(message = "Close price is required")
        @Positive(message = "Close price must be positive")
        BigDecimal closePrice,

        @NotNull(message = "Volume is required")
        @PositiveOrZero(message = "Volume cannot be negative")
        Long volume
) {
}

