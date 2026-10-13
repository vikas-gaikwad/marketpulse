package com.marketpulse.marketdata.repository;

import com.marketpulse.marketdata.entity.DailyMarketData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyMarketDataRepository
        extends JpaRepository<DailyMarketData, Long> {

    Optional<DailyMarketData> findTopBySymbolOrderByTradingDateDesc(
            String symbol);

    List<DailyMarketData> findBySymbolAndTradingDateBetweenOrderByTradingDateAsc(
            String symbol,
            LocalDate from,
            LocalDate to);

    boolean existsBySymbolAndTradingDate(
            String symbol,
            LocalDate tradingDate);
}

