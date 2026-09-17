package com.marketpulse.stock.dto;

public class StockResponse {
    private Long id;
    private String symbol;
    private String companyName;
    private String exchange;
    private String sector;

    public StockResponse() {
    }

    public StockResponse(Long id, String symbol, String companyName,
                         String exchange, String sector) {
        this.id = id;
        this.symbol = symbol;
        this.companyName = companyName;
        this.exchange = exchange;
        this.sector = sector;
    }

    public Long getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getExchange() {
        return exchange;
    }

    public String getSector() {
        return sector;
    }
}
