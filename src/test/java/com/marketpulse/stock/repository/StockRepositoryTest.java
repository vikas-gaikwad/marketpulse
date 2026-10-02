package com.marketpulse.stock.repository;


//import com.marketpulse.stock.entity.Stock;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
//import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;

//@DataJpaTest
//@AutoConfigureTestDatabase(
//        replace = AutoConfigureTestDatabase.Replace.NONE
//)
class StockRepositoryTest {

    /*@Autowired
    private StockRepository stockRepository;

    @BeforeEach
    void setUp() {

        stockRepository.deleteAll();

        Stock tcs = new Stock();
        tcs.setSymbol("TCS");
        tcs.setCompanyName("TCS Limited");
        tcs.setExchange("NSE");
        tcs.setSector("IT Services");

        Stock infosys = new Stock();
        infosys.setSymbol("INFOSYS");
        infosys.setCompanyName("Infosys Limited");
        infosys.setExchange("NSE");
        infosys.setSector("Information Technology");

        Stock techm = new Stock();
        techm.setSymbol("TECHM");
        techm.setCompanyName("Tech Mahindra");
        techm.setExchange("NSE");
        techm.setSector("Information Technology");

        Stock reliance = new Stock();
        reliance.setSymbol("RELIANCE");
        reliance.setCompanyName("Reliance Industries");
        reliance.setExchange("NSE");
        reliance.setSector("Energy");

        stockRepository.saveAll(
                List.of(tcs, infosys, techm, reliance)
        );
    }

    @Test
    void shouldFindStockBySymbol() {

        Optional<Stock> result =
                stockRepository.findBySymbol("TCS");

        assertTrue(result.isPresent());
        assertEquals("TCS", result.get().getSymbol());
        assertEquals("TCS Limited",
                result.get().getCompanyName());
    }

    @Test
    void shouldReturnEmptyWhenSymbolDoesNotExist() {

        Optional<Stock> result =
                stockRepository.findBySymbol("ABC");

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldSupportPagination() {

        Pageable pageable =
                PageRequest.of(
                        0,
                        2,
                        Sort.by("symbol").ascending()
                );

        Page<Stock> result =
                stockRepository.findAll(pageable);

        assertEquals(2, result.getContent().size());
        assertEquals(4, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        assertEquals(0, result.getNumber());
        assertEquals(2, result.getSize());
    }

    @Test
    void shouldSupportSortingBySymbol() {

        Pageable pageable =
                PageRequest.of(
                        0,
                        10,
                        Sort.by("symbol").ascending()
                );

        Page<Stock> result =
                stockRepository.findAll(pageable);

        List<Stock> stocks = result.getContent();

        assertEquals("INFOSYS",
                stocks.get(0).getSymbol());

        assertEquals("RELIANCE",
                stocks.get(1).getSymbol());

        assertEquals("TCS",
                stocks.get(2).getSymbol());

        assertEquals("TECHM",
                stocks.get(3).getSymbol());
    }

    @Test
    void shouldSaveStock() {

        Stock stock = new Stock();

        stock.setSymbol("HDFCBANK");
        stock.setCompanyName("HDFC Bank");
        stock.setExchange("NSE");
        stock.setSector("Banking");

        Stock saved =
                stockRepository.save(stock);

        assertNotNull(saved.getId());
        assertEquals("HDFCBANK", saved.getSymbol());
        assertEquals("HDFC Bank",
                saved.getCompanyName());
    }

    @Test
    void shouldDeleteStock() {

        Optional<Stock> stock =
                stockRepository.findBySymbol("TCS");

        assertTrue(stock.isPresent());

        stockRepository.delete(stock.get());

        Optional<Stock> result =
                stockRepository.findBySymbol("TCS");

        assertTrue(result.isEmpty());
    }*/
}
