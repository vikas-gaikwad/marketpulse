package com.marketpulse.stock.dto;

public class StockUpdateRequest {
    private String companyName;
    private String exchange;
    private String sector;

    public StockUpdateRequest() {
    }

    public StockUpdateRequest(String companyName, String exchange, String sector) {
        this.companyName = companyName;
        this.exchange = exchange;
        this.sector = sector;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }
}
