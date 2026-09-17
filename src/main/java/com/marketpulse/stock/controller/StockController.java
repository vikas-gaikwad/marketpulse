package com.marketpulse.stock.controller;

import com.marketpulse.stock.dto.StockResponse;
import com.marketpulse.stock.entity.Stock;
import com.marketpulse.stock.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
