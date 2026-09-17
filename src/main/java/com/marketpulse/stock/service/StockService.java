package com.marketpulse.stock.service;

import com.marketpulse.exception.StockNotFoundException;
import com.marketpulse.stock.dto.StockResponse;
import com.marketpulse.stock.entity.Stock;
import com.marketpulse.stock.repository.StockRepository;
import org.springframework.stereotype.Service;

@Service
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }
    public StockResponse getStockBySymbol(String symbol){
        Stock stock  = stockRepository.findBySymbol(symbol)
                .orElseThrow(()->
                        new StockNotFoundException("Stock not found: " + symbol));

        return new StockResponse(
                stock.getId(),
                stock.getSymbol(),
                stock.getCompanyName(),
                stock.getExchange(),
                stock.getSector()
        );
    }

}
