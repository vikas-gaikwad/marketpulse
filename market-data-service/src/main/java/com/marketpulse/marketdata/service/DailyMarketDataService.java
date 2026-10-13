package com.marketpulse.marketdata.service;

import com.marketpulse.marketdata.entity.DailyMarketData;
import com.marketpulse.marketdata.repository.DailyMarketDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import com.marketpulse.marketdata.exception.DuplicateMarketDataException;
import com.marketpulse.marketdata.exception.InvalidMarketDataException;
import com.marketpulse.marketdata.exception.MarketDataNotFoundException;

@Service
public class DailyMarketDataService {

    private final DailyMarketDataRepository repository;

    public DailyMarketDataService(DailyMarketDataRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public DailyMarketData create(DailyMarketData data) {
        String symbol = normalizeSymbol(data.getSymbol());
        data.setSymbol(symbol);

        validatePrices(data);

        if (data.getVolume() == null || data.getVolume() < 0) {
            throw new InvalidMarketDataException(
                    "Volume must be zero or greater");
        }

        if (repository.existsBySymbolAndTradingDate(
                symbol, data.getTradingDate())) {
            throw new DuplicateMarketDataException(
                    "Market data already exists for " + symbol
                            + " on " + data.getTradingDate());
        }

        return repository.save(data);
    }

    @Transactional(readOnly = true)
    public DailyMarketData getLatest(String symbol) {
        String normalizedSymbol = normalizeSymbol(symbol);

        return repository
                .findTopBySymbolOrderByTradingDateDesc(normalizedSymbol)
                .orElseThrow(() -> new MarketDataNotFoundException(
                        "No market data found for " + normalizedSymbol));
    }

    @Transactional(readOnly = true)
    public List<DailyMarketData> getHistory(
            String symbol, LocalDate from, LocalDate to) {

        String normalizedSymbol = normalizeSymbol(symbol);

        if (from == null || to == null) {
            throw new InvalidMarketDataException(
                    "Both from and to dates are required");
        }

        if (from.isAfter(to)) {
            throw new InvalidMarketDataException(
                    "From date must not be after to date");
        }

        return repository
                .findBySymbolAndTradingDateBetweenOrderByTradingDateAsc(
                        normalizedSymbol, from, to);
    }

    private String normalizeSymbol(String symbol) {
        if (symbol == null || symbol.isBlank()) {
            throw new InvalidMarketDataException(
                    "Stock symbol must not be blank");
        }

        return symbol.trim().toUpperCase(Locale.ROOT);
    }

    private void validatePrices(DailyMarketData data) {
        BigDecimal open = data.getOpenPrice();
        BigDecimal high = data.getHighPrice();
        BigDecimal low = data.getLowPrice();
        BigDecimal close = data.getClosePrice();

        if (open == null || high == null
                || low == null || close == null) {
            throw new InvalidMarketDataException(
                    "Open, high, low and close prices are required");
        }

        if (open.signum() <= 0 || high.signum() <= 0
                || low.signum() <= 0 || close.signum() <= 0) {
            throw new InvalidMarketDataException(
                    "All prices must be greater than zero");
        }

        if (high.compareTo(open) < 0
                || high.compareTo(close) < 0
                || high.compareTo(low) < 0) {
            throw new InvalidMarketDataException(
                    "High price must be greater than or equal to "
                            + "open, close and low prices");
        }

        if (low.compareTo(open) > 0
                || low.compareTo(close) > 0) {
            throw new InvalidMarketDataException(
                    "Low price must be less than or equal to "
                            + "open and close prices");
        }

        if (data.getTradingDate() == null) {
            throw new InvalidMarketDataException(
                    "Trading date is required");
        }
    }
}

