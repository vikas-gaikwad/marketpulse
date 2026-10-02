package com.marketpulse.stock.controller;

import com.marketpulse.exception.GlobalExceptionHandler;
import com.marketpulse.exception.StockAlreadyExistsException;
import com.marketpulse.exception.StockNotFoundException;
import com.marketpulse.stock.dto.StockRequest;
import com.marketpulse.stock.dto.StockResponse;
import com.marketpulse.stock.dto.StockUpdateRequest;
import com.marketpulse.stock.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class StockControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StockService stockService;

    @InjectMocks
    private StockController stockController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(stockController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(
                        new PageableHandlerMethodArgumentResolver()
                )
                .build();
    }
    @Test
    void shouldGetStockBySymbol() throws Exception {

        StockResponse stockResponse = new StockResponse(
                1L,
                "TCS",
                "TCS Limited",
                "NSE",
                "IT Services"
        );

        when(stockService.getStockBySymbol("TCS"))
                .thenReturn(stockResponse);

        mockMvc.perform(
                        get("/api/stocks/TCS")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.symbol").value("TCS"))
                .andExpect(jsonPath("$.companyName").value("TCS Limited"))
                .andExpect(jsonPath("$.exchange").value("NSE"))
                .andExpect(jsonPath("$.sector").value("IT Services"));

        verify(stockService).getStockBySymbol("TCS");
    }
    @Test
    void shouldCreateStock() throws Exception {

        StockRequest request = new StockRequest(
                "TCS",
                "TCS Limited",
                "NSE",
                "IT Services"
        );

        StockResponse response = new StockResponse(
                1L,
                "TCS",
                "TCS Limited",
                "NSE",
                "IT Services"
        );

        when(stockService.createStock(any(StockRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/stocks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "symbol": "TCS",
                                        "companyName": "TCS Limited",
                                        "exchange": "NSE",
                                        "sector": "IT Services"
                                    }
                                    """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.symbol").value("TCS"))
                .andExpect(jsonPath("$.companyName").value("TCS Limited"))
                .andExpect(jsonPath("$.exchange").value("NSE"))
                .andExpect(jsonPath("$.sector").value("IT Services"));

        verify(stockService).createStock(any(StockRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenCreateRequestIsInvalid() throws Exception {

        mockMvc.perform(
                        post("/api/stocks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "symbol": "",
                                        "companyName": "",
                                        "exchange": "NSE",
                                        "sector": "IT Services"
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists());

        verify(stockService, never())
                .createStock(any(StockRequest.class));
    }
    @Test
    void shouldReturnBadRequestWhenCompanyNameExceeds100Characters()
            throws Exception {

        String longCompanyName = "A".repeat(101);

        mockMvc.perform(
                        post("/api/stocks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "symbol": "TCS",
                                        "companyName": "%s",
                                        "exchange": "NSE",
                                        "sector": "IT Services"
                                    }
                                    """.formatted(longCompanyName))
                )
                .andExpect(status().isBadRequest());

        verify(stockService, never())
                .createStock(any(StockRequest.class));
    }
    @Test
    void shouldReturnConflictWhenStockAlreadyExists() throws Exception {

        when(stockService.createStock(any(StockRequest.class)))
                .thenThrow(
                        new StockAlreadyExistsException(
                                "Stock already exists: TCS"
                        )
                );

        mockMvc.perform(
                        post("/api/stocks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "symbol": "TCS",
                                        "companyName": "TCS Limited",
                                        "exchange": "NSE",
                                        "sector": "IT Services"
                                    }
                                    """)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message")
                        .value("Stock already exists: TCS"));

        verify(stockService)
                .createStock(any(StockRequest.class));
    }
    @Test
    void shouldReturnNotFoundWhenGettingStock() throws Exception {

        when(stockService.getStockBySymbol("ABC"))
                .thenThrow(
                        new StockNotFoundException(
                                "Stock not found: ABC"
                        )
                );

        mockMvc.perform(
                        get("/api/stocks/ABC")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Stock not found: ABC"));

        verify(stockService).getStockBySymbol("ABC");
    }
    @Test
    void shouldUpdateStockSuccessfully() throws Exception {

        StockResponse response = new StockResponse(
                1L,
                "TCS",
                "TCS Technologies",
                "BSE",
                "Technology"
        );

        when(stockService.updateStock(
                eq("TCS"),
                any(StockUpdateRequest.class)
        )).thenReturn(response);

        mockMvc.perform(
                        put("/api/stocks/TCS")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "companyName": "TCS Technologies",
                                        "exchange": "BSE",
                                        "sector": "Technology"
                                    }
                                    """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.symbol").value("TCS"))
                .andExpect(jsonPath("$.companyName")
                        .value("TCS Technologies"))
                .andExpect(jsonPath("$.exchange")
                        .value("BSE"))
                .andExpect(jsonPath("$.sector")
                        .value("Technology"));

        verify(stockService).updateStock(
                eq("TCS"),
                any(StockUpdateRequest.class)
        );
    }
    @Test
    void shouldReturnNotFoundWhenUpdatingStock() throws Exception {

        when(stockService.updateStock(
                eq("ABC"),
                any(StockUpdateRequest.class)
        )).thenThrow(
                new StockNotFoundException(
                        "Stock not found: ABC"
                )
        );

        mockMvc.perform(
                        put("/api/stocks/ABC")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "companyName": "ABC Limited",
                                        "exchange": "NSE",
                                        "sector": "Technology"
                                    }
                                    """)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Stock not found: ABC"));
    }
    @Test
    void shouldReturnBadRequestWhenUpdateRequestIsInvalid()
            throws Exception {

        mockMvc.perform(
                        put("/api/stocks/TCS")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "companyName": "",
                                        "exchange": "",
                                        "sector": ""
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists());

        verify(stockService, never())
                .updateStock(
                        anyString(),
                        any(StockUpdateRequest.class)
                );
    }
    @Test
    void shouldDeleteStockSuccessfully() throws Exception {

        doNothing()
                .when(stockService)
                .deleteStock("TCS");

        mockMvc.perform(
                        delete("/api/stocks/TCS")
                )
                .andExpect(status().isNoContent());

        verify(stockService)
                .deleteStock("TCS");
    }
    @Test
    void shouldReturnNotFoundWhenDeletingStock() throws Exception {

        doThrow(
                new StockNotFoundException(
                        "Stock not found: ABC"
                )
        ).when(stockService)
                .deleteStock("ABC");

        mockMvc.perform(
                        delete("/api/stocks/ABC")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Stock not found: ABC"));

        verify(stockService)
                .deleteStock("ABC");
    }
    @Test
    void shouldSearchStocksWithPaginationFilteringAndSorting()
            throws Exception {

        StockResponse response = new StockResponse(
                1L,
                "TECHM",
                "Tech Mahindra",
                "NSE",
                "Information Technology"
        );

        Page<StockResponse> page =
                new PageImpl<>(
                        List.of(response),
                        PageRequest.of(
                                0,
                                10,
                                Sort.by("symbol").ascending()
                        ),
                        1
                );

        when(stockService.searchStocks(
                eq("tec"),
                eq("NSE"),
                eq("Information Technology"),
                any(Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(
                        get("/api/stocks")
                                .param("symbol", "tec")
                                .param("exchange", "NSE")
                                .param("sector", "Information Technology")
                                .param("page", "0")
                                .param("size", "10")
                                .param("sort", "symbol,asc")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].symbol")
                        .value("TECHM"))
                .andExpect(jsonPath("$.totalElements")
                        .value(1))
                .andExpect(jsonPath("$.size")
                        .value(10));

        verify(stockService).searchStocks(
                eq("tec"),
                eq("NSE"),
                eq("Information Technology"),
                any(Pageable.class)
        );
    }
    @Test
    void shouldReturnBadRequestForInvalidSortField()
            throws Exception {

        mockMvc.perform(
                        get("/api/stocks")
                                .param("sort", "price,desc")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Invalid sort field: price"));

        verifyNoInteractions(stockService);
    }
}
