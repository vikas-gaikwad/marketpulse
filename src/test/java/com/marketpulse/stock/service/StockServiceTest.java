package com.marketpulse.stock.service;

import com.marketpulse.exception.StockAlreadyExistsException;
import com.marketpulse.exception.StockNotFoundException;
import com.marketpulse.stock.dto.StockRequest;
import com.marketpulse.stock.dto.StockResponse;
import com.marketpulse.stock.dto.StockUpdateRequest;
import com.marketpulse.stock.entity.Stock;
import com.marketpulse.stock.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    private StockService stockService;

    @BeforeEach
    void setUp() {
        stockService = new StockService(stockRepository);
    }

    @Test
    void shouldCreateStockService() {
        // Arrange
        StockRequest request = new StockRequest(
                "TCS",
                "TCS Limited",
                "NSE",
                "IT Services"
        );
        Stock savedStock = new Stock();
        savedStock.setId(1L);
        savedStock.setSymbol("TCS");
        savedStock.setCompanyName("TCS Limited");
        savedStock.setExchange("NSE");
        savedStock.setSector("IT Services");

        //We're telling Mockito:
        //When the service asks the repository for TCS, pretend it doesn't exist.
        when(stockRepository.findBySymbol("TCS"))
                .thenReturn(Optional.empty());

        //We're telling Mockito:
        //Pretend the database successfully saved the stock and returned this entity.
        when(stockRepository.save(any(Stock.class)))
                .thenReturn(savedStock);

        // Act
        //This executes the real StockService
        StockResponse response = stockService.createStock(request);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        //We're checking the result.
        assertEquals("TCS", response.getSymbol());
        assertEquals("TCS Limited", response.getCompanyName());
        assertEquals("NSE", response.getExchange());
        assertEquals("IT Services", response.getSector());

        //We're checking that the service actually called the repository.
        verify(stockRepository).findBySymbol("TCS");
        verify(stockRepository).save(any(Stock.class));


    }
    @Test
    void shouldThrowExceptionWhenStockAlreadyExists() {

        // Arrange
        StockRequest request = new StockRequest(
                "TCS",
                "TCS Limited",
                "NSE",
                "IT Services"
        );

        Stock existingStock = new Stock();
        existingStock.setId(1L);
        existingStock.setSymbol("TCS");

        when(stockRepository.findBySymbol("TCS"))
                .thenReturn(Optional.of(existingStock));

        // Act & Assert
        assertThrows(
                StockAlreadyExistsException.class,
                () -> stockService.createStock(request)
        );

        // Verify save was never called
        verify(stockRepository, never())
                .save(any(Stock.class));
    }
    @Test
    void shouldGetStockBySymbolSuccessfully() {

        // Arrange
        Stock stock = new Stock();
        stock.setId(1L);
        stock.setSymbol("TCS");
        stock.setCompanyName("TCS Limited");
        stock.setExchange("NSE");
        stock.setSector("IT Services");

        when(stockRepository.findBySymbol("TCS"))
                .thenReturn(Optional.of(stock));

        // Act
        StockResponse response =
                stockService.getStockBySymbol("TCS");

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("TCS", response.getSymbol());
        assertEquals("TCS Limited", response.getCompanyName());
        assertEquals("NSE", response.getExchange());
        assertEquals("IT Services", response.getSector());

        verify(stockRepository).findBySymbol("TCS");
    }
    @Test
    void shouldThrowExceptionWhenStockNotFound() {

        // Arrange
        when(stockRepository.findBySymbol("ABC"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                StockNotFoundException.class,
                () -> stockService.getStockBySymbol("ABC")
        );

        verify(stockRepository).findBySymbol("ABC");
    }
    @Test
    void shouldUpdateStockSuccessfully() {

        // Arrange
        Stock existingStock = new Stock();
        existingStock.setId(1L);
        existingStock.setSymbol("TCS");
        existingStock.setCompanyName("TCS Limited");
        existingStock.setExchange("NSE");
        existingStock.setSector("IT Services");

        StockUpdateRequest request = new StockUpdateRequest(
                "TCS Technologies",
                "BSE",
                "Technology"
        );

        when(stockRepository.findBySymbol("TCS"))
                .thenReturn(Optional.of(existingStock));

        when(stockRepository.save(any(Stock.class)))
                .thenReturn(existingStock);

        // Act
        StockResponse response =
                stockService.updateStock("TCS", request);

        // Assert
        assertNotNull(response);
        assertEquals("TCS", response.getSymbol());
        assertEquals("TCS Technologies", response.getCompanyName());
        assertEquals("BSE", response.getExchange());
        assertEquals("Technology", response.getSector());

        verify(stockRepository).findBySymbol("TCS");
        verify(stockRepository).save(existingStock);
    }
    @Test
    void shouldThrowExceptionWhenUpdatingStockNotFound() {

        // Arrange
        StockUpdateRequest request = new StockUpdateRequest(
                "TCS Technologies",
                "BSE",
                "Technology"
        );

        when(stockRepository.findBySymbol("ABC"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                StockNotFoundException.class,
                () -> stockService.updateStock("ABC", request)
        );

        verify(stockRepository).findBySymbol("ABC");
        verify(stockRepository, never())
                .save(any(Stock.class));
    }
    @Test
    void shouldDeleteStockSuccessfully() {

        // Arrange
        Stock existingStock = new Stock();
        existingStock.setId(1L);
        existingStock.setSymbol("TCS");

        when(stockRepository.findBySymbol("TCS"))
                .thenReturn(Optional.of(existingStock));

        // Act
        stockService.deleteStock("TCS");

        // Assert
        verify(stockRepository).findBySymbol("TCS");
        verify(stockRepository).delete(existingStock);
    }
    @Test
    void shouldThrowExceptionWhenDeletingStockNotFound() {

        // Arrange
        when(stockRepository.findBySymbol("ABC"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                StockNotFoundException.class,
                () -> stockService.deleteStock("ABC")
        );

        verify(stockRepository).findBySymbol("ABC");

        verify(stockRepository, never())
                .delete(any(Stock.class));
    }
    @Test
    void shouldSearchStocksSuccessfully() {

        // Arrange
        Stock stock1 = new Stock();
        stock1.setId(1L);
        stock1.setSymbol("TCS");
        stock1.setCompanyName("TCS Limited");
        stock1.setExchange("NSE");
        stock1.setSector("IT Services");

        Stock stock2 = new Stock();
        stock2.setId(2L);
        stock2.setSymbol("TECHM");
        stock2.setCompanyName("Tech Mahindra");
        stock2.setExchange("NSE");
        stock2.setSector("Information Technology");

        List<Stock> stocks = List.of(stock1, stock2);

        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.by("symbol").ascending()
        );

        Page<Stock> stockPage =
                new PageImpl<>(stocks, pageable, stocks.size());

        when(stockRepository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(stockPage);

        // Act
        Page<StockResponse> response =
                stockService.searchStocks(
                        "T",
                        "NSE",
                        null,
                        pageable
                );

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getTotalElements());
        assertEquals(2, response.getContent().size());

        assertEquals("TCS", response.getContent().get(0).getSymbol());
        assertEquals("TECHM", response.getContent().get(1).getSymbol());

        verify(stockRepository).findAll(
                any(Specification.class),
                eq(pageable)
        );
    }
}