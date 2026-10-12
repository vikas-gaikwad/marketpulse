package com.marketpulse.stock.controller;


import com.marketpulse.stock.dto.StockRequest;
import com.marketpulse.stock.dto.StockResponse;
import com.marketpulse.stock.dto.StockUpdateRequest;
import com.marketpulse.stock.service.StockService;
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

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    private static final Set<String> ALLOWED_SORT_FIELDS =
            Set.of("symbol", "companyName", "exchange", "sector");


    // ============================================================
    // GET STOCK BY SYMBOL
    // ============================================================

    @Operation(
            summary = "Get stock by symbol",
            description = "Retrieves a stock using its unique stock symbol."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Stock found successfully",
                    content = @Content(
                            schema = @Schema(implementation = StockResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Stock not found"
            )
    })
    @GetMapping("/{symbol}")
    public ResponseEntity<StockResponse> getStockBySymbol(

            @Parameter(
                    description = "Unique stock symbol",
                    example = "TCS",
                    required = true
            )
            @PathVariable String symbol) {

        StockResponse stockResponse =
                stockService.getStockBySymbol(symbol);

        return ResponseEntity.ok(stockResponse);
    }


    // ============================================================
    // CREATE STOCK
    // ============================================================

    @Operation(
            summary = "Create a new stock",
            description = "Creates a new stock using the supplied stock information."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Stock created successfully",
                    content = @Content(
                            schema = @Schema(implementation = StockResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid stock data"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Stock with the same symbol already exists"
            )
    })
    @PostMapping
    public ResponseEntity<StockResponse> createStock(

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Stock information",
                    required = true
            )
            @RequestBody @Valid StockRequest stockRequest) {

        StockResponse stockResponse =
                stockService.createStock(stockRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(stockResponse);
    }


    // ============================================================
    // GET ALL STOCKS
    // ============================================================

    @Parameter(
            name = "page",
            in = ParameterIn.QUERY,
            description = "Zero-based page number",
            schema = @Schema(type = "integer", defaultValue = "0")
    )
    @Parameter(
            name = "size",
            in = ParameterIn.QUERY,
            description = "Number of records per page (maximum 50)",
            schema = @Schema(type = "integer", defaultValue = "10")
    )
    @Parameter(
            name = "sort",
            in = ParameterIn.QUERY,
            description = "Sort field and direction, e.g. symbol,asc",
            array = @ArraySchema(
                    schema = @Schema(type = "string"),
                    arraySchema = @Schema(type = "array")
            )
    )
    @GetMapping
    public ResponseEntity<Page<StockResponse>> getAllStocks(

            @RequestParam(required = false)
            String symbol,

            @RequestParam(required = false)
            String exchange,

            @RequestParam(required = false)
            String sector,

            @PageableDefault(size = 10, page = 0)
            Pageable pageable) {

        for (var order : pageable.getSort()) {

            if (!ALLOWED_SORT_FIELDS.contains(order.getProperty())) {
                throw new IllegalArgumentException(
                        "Invalid sort field: " + order.getProperty()
                );
            }
        }

        if (pageable.getPageSize() > 50) {

            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    50,
                    pageable.getSort()
            );
        }

        Page<StockResponse> stockResponse;

        if ((symbol != null && !symbol.isBlank())
                || (exchange != null && !exchange.isBlank())
                || (sector != null && !sector.isBlank())) {

            stockResponse = stockService.searchStocks(
                    symbol,
                    exchange,
                    sector,
                    pageable
            );

        } else {

            stockResponse = stockService.getAllStocks(pageable);
        }

        return ResponseEntity.ok(stockResponse);
    }


    // ============================================================
    // UPDATE STOCK
    // ============================================================

    @Operation(
            summary = "Update stock",
            description = "Updates an existing stock using its symbol."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Stock updated successfully",
                    content = @Content(
                            schema = @Schema(implementation = StockResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid stock data"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Stock not found"
            )
    })
    @PutMapping("/{symbol}")
    public ResponseEntity<StockResponse> updateStock(

            @Parameter(
                    description = "Stock symbol to update",
                    example = "TCS",
                    required = true
            )
            @PathVariable String symbol,

            @RequestBody @Valid StockUpdateRequest stockUpdateRequest) {

        StockResponse stockResponse =
                stockService.updateStock(
                        symbol,
                        stockUpdateRequest
                );

        return ResponseEntity.ok(stockResponse);
    }


    // ============================================================
    // DELETE STOCK
    // ============================================================

    @Operation(
            summary = "Delete stock",
            description = "Deletes an existing stock using its symbol."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Stock deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Stock not found"
            )
    })
    @DeleteMapping("/{symbol}")
    public ResponseEntity<Void> deleteStock(

            @Parameter(
                    description = "Stock symbol to delete",
                    example = "TCS",
                    required = true
            )
            @PathVariable String symbol) {

        stockService.deleteStock(symbol);

        return ResponseEntity.noContent().build();
    }
}