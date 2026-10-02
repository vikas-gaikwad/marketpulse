package com.marketpulse.stock.specification;

import com.marketpulse.stock.entity.Stock;
import org.springframework.data.jpa.domain.Specification;

public class StockSpecification {

    public static Specification<Stock> symbolContains(String symbol) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("symbol")),
                        "%" + symbol.toLowerCase() + "%"
                );
    }

    public static Specification<Stock> exchangeEquals(String exchange) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("exchange")),
                        exchange.toLowerCase()
                );
    }

    public static Specification<Stock> sectorEquals(String sector) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("sector")),
                        sector.toLowerCase()
                );
    }
}
