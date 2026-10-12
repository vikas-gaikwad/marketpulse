package com.marketpulse.stockservice.specification;

import com.marketpulse.stockservice.entity.Stock;
import org.springframework.data.jpa.domain.Specification;

public class StockSpecification {

    public static Specification<Stock> symbolContains(String symbol) {
        if (symbol == null || symbol.isBlank()) {
            return null;
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("symbol")),
                        "%" + symbol.trim().toLowerCase() + "%"
                );
    }

    public static Specification<Stock> exchangeEquals(String exchange) {
        if (exchange == null || exchange.isBlank()) {
            return null;
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("exchange")),
                        exchange.trim().toLowerCase()
                );
    }

    public static Specification<Stock> sectorEquals(String sector) {
        if (sector == null || sector.isBlank()) {
            return null;
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("sector")),
                        sector.trim().toLowerCase()
                );
    }

}
