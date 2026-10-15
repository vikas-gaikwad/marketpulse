
package com.marketpulse.analytics.service;

import com.marketpulse.analytics.client.MarketDataClient;
import com.marketpulse.analytics.dto.MarketDataResponse;
import com.marketpulse.analytics.indicator.EmaCalculator;
import com.marketpulse.analytics.indicator.RsiCalculator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Service
public class AnalyticsService {

    private final MarketDataClient marketDataClient;
    private final EmaCalculator emaCalculator;
    private final RsiCalculator rsiCalculator;

    public AnalyticsService(MarketDataClient marketDataClient) {
        this.marketDataClient = marketDataClient;
        this.emaCalculator = new EmaCalculator();
        this.rsiCalculator = new RsiCalculator();
    }

    public BigDecimal calculateEma(
            String symbol, int period,
            LocalDate from, LocalDate to) {

        if (symbol == null || symbol.isBlank()) {
            throw new IllegalArgumentException(
                    "Stock symbol must not be blank");
        }

        if (period <= 0) {
            throw new IllegalArgumentException(
                    "EMA period must be greater than zero");
        }

        if (from == null || to == null || from.isAfter(to)) {
            throw new IllegalArgumentException(
                    "A valid from/to date range is required");
        }

        List<MarketDataResponse> history =
                marketDataClient.getHistoricalData(
                        symbol.trim().toUpperCase(Locale.ROOT), from, to);

        List<BigDecimal> closingPrices = history.stream()
                .map(MarketDataResponse::closePrice)
                .toList();

        return emaCalculator.calculate(closingPrices, period);
    }
    public BigDecimal calculateRsi(
            String symbol,
            int period,
            LocalDate from,
            LocalDate to) {

        if (symbol == null || symbol.isBlank()) {
            throw new IllegalArgumentException("Stock symbol must not be blank");
        }

        if (period <= 0) {
            throw new IllegalArgumentException("RSI period must be greater than zero");
        }

        if (from == null || to == null || from.isAfter(to)) {
            throw new IllegalArgumentException(
                    "A valid from/to date range is required");
        }

        List<MarketDataResponse> history =
                marketDataClient.getHistoricalData(
                        symbol.trim().toUpperCase(Locale.ROOT),
                        from,
                        to);

        List<BigDecimal> closingPrices = history.stream()
                .map(MarketDataResponse::closePrice)
                .toList();

        return rsiCalculator.calculate(closingPrices, period);
    }
}
