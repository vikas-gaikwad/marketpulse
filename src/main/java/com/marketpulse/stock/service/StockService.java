package com.marketpulse.stock.service;

import com.marketpulse.exception.StockNotFoundException;
import com.marketpulse.stock.dto.StockRequest;
import com.marketpulse.stock.dto.StockResponse;
import com.marketpulse.stock.entity.Stock;
import com.marketpulse.stock.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public StockResponse createStock(StockRequest stockRequest) {
        Stock stock = new Stock();

        stock.setSymbol(stockRequest.getSymbol());
        stock.setCompanyName(stockRequest.getCompanyName());
        stock.setExchange(stockRequest.getExchange());
        stock.setSector(stockRequest.getSector());

        Stock savedStock = stockRepository.save(stock);
        // Now convert savedStock → StockResponse
        return new StockResponse(
                savedStock.getId(),
                savedStock.getSymbol(),
                savedStock.getCompanyName(),
                savedStock.getExchange(),
                savedStock.getSector()
        );


    }

    public List<StockResponse> getAllStocks() {

        List<Stock> stocks = stockRepository.findAll();

        // convert List<Stock> → List<StockResponse>
        return stocks.stream()
                .map(stock -> new StockResponse(
                        stock.getId(),
                        stock.getSymbol(),
                        stock.getCompanyName(),
                        stock.getExchange(),
                        stock.getSector()
                ))
                .toList();
    }

}
