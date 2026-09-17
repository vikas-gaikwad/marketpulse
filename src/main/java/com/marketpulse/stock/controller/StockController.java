package com.marketpulse.stock.controller;

import com.marketpulse.stock.dto.StockRequest;
import com.marketpulse.stock.dto.StockResponse;
import com.marketpulse.stock.dto.StockUpdateRequest;
import com.marketpulse.stock.service.StockService;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//It tells Spring that this class handles REST requests and its methods return data for the HTTP response.
@RequestMapping("/api/stocks")
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<StockResponse> getStockBySymbol(@PathVariable String symbol){
        StockResponse stockResponse =this.stockService.getStockBySymbol(symbol);
        return ResponseEntity.ok(stockResponse);
    }
    @PostMapping
    public ResponseEntity<StockResponse> createStock(
            @RequestBody StockRequest stockRequest) {
        // We will call the service here
        StockResponse stockResponse= this.stockService.createStock(stockRequest);
        return  ResponseEntity.status(HttpStatus.CREATED).body(stockResponse);
    }
    @GetMapping
    public ResponseEntity<List<StockResponse>> getAllStocks() {
        // call service
        List<StockResponse> stockResponse=this.stockService.getAllStocks();
        return ResponseEntity.ok(stockResponse);
    }
    @PutMapping("/{symbol}")
    public ResponseEntity<StockResponse> updateStock(@PathVariable String symbol, @RequestBody StockUpdateRequest stockUpdateRequest) {
        StockResponse stockResponse = stockService.updateStock(symbol, stockUpdateRequest);
        return ResponseEntity.ok(stockResponse);
    }
}
