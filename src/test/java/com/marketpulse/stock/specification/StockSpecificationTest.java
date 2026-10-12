package com.marketpulse.stock.specification;

import com.marketpulse.stock.entity.Stock;
import com.marketpulse.stock.specification.StockSpecification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockSpecificationTest {

    @SuppressWarnings("unchecked")
    private Root<Stock> mockRoot() {
        return mock(Root.class);
    }

    @SuppressWarnings("unchecked")
    private Path<String> mockStringPath() {
        return mock(Path.class);
    }

    @SuppressWarnings("unchecked")
    private CriteriaQuery<?> mockQuery() {
        return mock(CriteriaQuery.class);
    }

    private CriteriaBuilder mockCriteriaBuilder() {
        return mock(CriteriaBuilder.class);
    }

    @Test
    void shouldCreateSymbolContainsSpecification() {
        assertNotNull(StockSpecification.symbolContains("tec"));
    }

    @Test
    void shouldCreateExchangeEqualsSpecification() {
        assertNotNull(StockSpecification.exchangeEquals("NSE"));
    }

    @Test
    void shouldCreateSectorEqualsSpecification() {
        assertNotNull(
                StockSpecification.sectorEquals("Information Technology")
        );
    }

    @Test
    void shouldExecuteSymbolContainsPredicate() {
        Root<Stock> root = mockRoot();
        CriteriaQuery<?> query = mockQuery();
        CriteriaBuilder cb = mockCriteriaBuilder();
        Path<String> path = mockStringPath();
        Predicate predicate = mock(Predicate.class);

        when(root.<String>get("symbol")).thenReturn(path);
        when(cb.lower(path)).thenReturn(path);
        when(cb.like(path, "%tec%")).thenReturn(predicate);

        Specification<Stock> specification =
                StockSpecification.symbolContains(" TEC ");

        Predicate result = specification.toPredicate(root, query, cb);

        assertSame(predicate, result);
        verify(cb).like(path, "%tec%");
    }

    @Test
    void shouldExecuteExchangeEqualsPredicate() {
        Root<Stock> root = mockRoot();
        CriteriaQuery<?> query = mockQuery();
        CriteriaBuilder cb = mockCriteriaBuilder();
        Path<String> path = mockStringPath();
        Predicate predicate = mock(Predicate.class);

        when(root.<String>get("exchange")).thenReturn(path);
        when(cb.lower(path)).thenReturn(path);
        when(cb.equal(path, "nse")).thenReturn(predicate);

        Specification<Stock> specification =
                StockSpecification.exchangeEquals(" NSE ");

        Predicate result = specification.toPredicate(root, query, cb);

        assertSame(predicate, result);
        verify(cb).equal(path, "nse");
    }

    @Test
    void shouldExecuteSectorEqualsPredicate() {
        Root<Stock> root = mockRoot();
        CriteriaQuery<?> query = mockQuery();
        CriteriaBuilder cb = mockCriteriaBuilder();
        Path<String> path = mockStringPath();
        Predicate predicate = mock(Predicate.class);

        when(root.<String>get("sector")).thenReturn(path);
        when(cb.lower(path)).thenReturn(path);
        when(cb.equal(path, "information technology"))
                .thenReturn(predicate);

        Specification<Stock> specification =
                StockSpecification.sectorEquals(" Information Technology ");

        Predicate result = specification.toPredicate(root, query, cb);

        assertSame(predicate, result);
        verify(cb).equal(path, "information technology");
    }

    @Test
    void shouldReturnNullForBlankSymbol() {
        assertNull(StockSpecification.symbolContains(""));
    }

    @Test
    void shouldReturnNullForNullSymbol() {
        assertNull(StockSpecification.symbolContains(null));
    }

    @Test
    void shouldReturnNullForBlankExchange() {
        assertNull(StockSpecification.exchangeEquals(" "));
    }

    @Test
    void shouldReturnNullForBlankSector() {
        assertNull(StockSpecification.sectorEquals(""));
    }
    @Test
    void shouldReturnNullForNullExchange() {
        assertNull(StockSpecification.exchangeEquals(null));
    }
}
