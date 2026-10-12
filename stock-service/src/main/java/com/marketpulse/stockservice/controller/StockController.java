package com.marketpulse.stockservice.controller;


import com.marketpulse.stockservice.dto.StockRequest;
import com.marketpulse.stockservice.dto.StockResponse;
import com.marketpulse.stockservice.dto.StockUpdateRequest;
import com.marketpulse.stockservice.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@RequestMapping("/api/stocks")
@Tag(
        name = "Stock API",
        description = "APIs for creating, retrieving, updating and deleting stocks"
)
public class StockController {

    private final StockService stockService;

    private static final Set<String> ALLOWED_SORT_FIELDS =
            Set.of("symbol", "companyName", "exchange", "sector");

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/{symbol}")
    @Operation(summary = "Get stock by symbol")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock found successfully"),
            @ApiResponse(responseCode = "404", description = "Stock not found")
    })
    public ResponseEntity<StockResponse> getStockBySymbol(
            @PathVariable String symbol) {

        return ResponseEntity.ok(stockService.getStockBySymbol(symbol));
    }

    @PostMapping
    @Operation(summary = "Create a new stock")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Stock created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid stock data"),
            @ApiResponse(responseCode = "409", description = "Stock already exists")
    })
    public ResponseEntity<StockResponse> createStock(
            @RequestBody @Valid StockRequest stockRequest) {

        StockResponse response = stockService.createStock(stockRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<StockResponse>> getAllStocks(
            @RequestParam(required = false) String symbol,
            @RequestParam(required = false) String exchange,
            @RequestParam(required = false) String sector,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {

        for (var order : pageable.getSort()) {
            if (!ALLOWED_SORT_FIELDS.contains(order.getProperty())) {
                throw new IllegalArgumentException(
                        "Invalid sort field: " + order.getProperty());
            }
        }

        if (pageable.getPageSize() > 50) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(), 50, pageable.getSort());
        }

        boolean hasFilters =
                (symbol != null && !symbol.isBlank())
                        || (exchange != null && !exchange.isBlank())
                        || (sector != null && !sector.isBlank());

        Page<StockResponse> response = hasFilters
                ? stockService.searchStocks(symbol, exchange, sector, pageable)
                : stockService.getAllStocks(pageable);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{symbol}")
    @Operation(summary = "Update stock")
    public ResponseEntity<StockResponse> updateStock(
            @PathVariable String symbol,
            @RequestBody @Valid StockUpdateRequest request) {

        return ResponseEntity.ok(
                stockService.updateStock(symbol, request));
    }

    @DeleteMapping("/{symbol}")
    @Operation(summary = "Delete stock")
    public ResponseEntity<Void> deleteStock(@PathVariable String symbol) {

        stockService.deleteStock(symbol);

        return ResponseEntity.noContent().build();
    }
}
