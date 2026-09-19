package com.marketpulse.stock.controller;

import com.marketpulse.exception.ErrorResponse;
import com.marketpulse.stock.dto.StockRequest;
import com.marketpulse.stock.dto.StockResponse;
import com.marketpulse.stock.dto.StockUpdateRequest;
import com.marketpulse.stock.service.StockService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
//It tells Spring that this class handles REST requests and its methods return data for the HTTP response.
@RequestMapping("/api/stocks")
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    private static final Set<String> ALLOWED_SORT_FIELDS =
            Set.of("symbol", "companyName", "exchange", "sector");

    @GetMapping("/{symbol}")
    public ResponseEntity<StockResponse> getStockBySymbol(@PathVariable String symbol) {
        StockResponse stockResponse = this.stockService.getStockBySymbol(symbol);
        return ResponseEntity.ok(stockResponse);
    }

    @PostMapping
    public ResponseEntity<StockResponse> createStock(
            @RequestBody @Valid StockRequest stockRequest) {
        // We will call the service here
        StockResponse stockResponse = this.stockService.createStock(stockRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(stockResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAllStocks(
            @PageableDefault(size = 10, page = 0) Pageable pageable) {

        for (Sort.Order order : pageable.getSort()) {

            if (!ALLOWED_SORT_FIELDS.contains(order.getProperty())) {

                ErrorResponse errorResponse = new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "Invalid sort field: " + order.getProperty()
                );

                return ResponseEntity.badRequest().body(errorResponse);
            }
        }

        if (pageable.getPageSize() > 50) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    50,
                    pageable.getSort()
            );
        }

        Page<StockResponse> stockResponse =
                stockService.getAllStocks(pageable);

        return ResponseEntity.ok(stockResponse);
    }

    @PutMapping("/{symbol}")
    public ResponseEntity<StockResponse> updateStock(@PathVariable String symbol, @RequestBody @Valid StockUpdateRequest stockUpdateRequest) {
        StockResponse stockResponse = stockService.updateStock(symbol, stockUpdateRequest);
        return ResponseEntity.ok(stockResponse);
    }

    @DeleteMapping("/{symbol}")
    public ResponseEntity<Void> deleteStock(@PathVariable String symbol) {
        // call service
        this.stockService.deleteStock(symbol);
        // return 204
        return ResponseEntity.noContent().build();

    }
}
