package com.marketpulse.stockservice.repository;

import com.marketpulse.stockservice.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long>,
        JpaSpecificationExecutor<Stock> {

        Optional<Stock> findBySymbol(String symbol);

        boolean existsBySymbol(String symbol);
}
