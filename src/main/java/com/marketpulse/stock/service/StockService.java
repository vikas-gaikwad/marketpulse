package com.marketpulse.stock.service;

import com.marketpulse.exception.StockAlreadyExistsException;
import com.marketpulse.exception.StockNotFoundException;
import com.marketpulse.stock.dto.StockRequest;
import com.marketpulse.stock.dto.StockResponse;
import com.marketpulse.stock.dto.StockUpdateRequest;
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
        if (this.stockRepository.findBySymbol(stockRequest.getSymbol()).isPresent()){
            throw new StockAlreadyExistsException("Stock already exists: "+ stockRequest.getSymbol());
        }
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

    public StockResponse updateStock(String symbol, StockUpdateRequest stockUpdateRequest) {

        // find stock
        Stock stock = stockRepository.findBySymbol(symbol)
                .orElseThrow(()->new StockNotFoundException("Stock not found"+symbol));
        // update fields
        stock.setCompanyName(stockUpdateRequest.getCompanyName());
        stock.setSector(stockUpdateRequest.getSector());
        stock.setExchange(stockUpdateRequest.getExchange());

        // save
        Stock updatedStock=this.stockRepository.save(stock);
        // return StockResponse
        return new StockResponse(
                updatedStock.getId(),
                updatedStock.getSymbol(),
                updatedStock.getCompanyName(),
                updatedStock.getExchange(),
                updatedStock.getSector()
        );
    }
    public void deleteStock(String symbol) {
        // find stock by symbol
        Stock stock = this.stockRepository.findBySymbol(symbol)
                .orElseThrow(()-> new StockNotFoundException("Stock not found : "+symbol));
        // delete the existing stock
        this.stockRepository.delete(stock);
    }
}
