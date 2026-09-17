package com.marketpulse.stock.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class StockUpdateRequest {
    @NotBlank(message = "Company name must not be blank")
    @Size(min = 2, max = 100, message = "Company name must be between 2 and 100 characters")
    private String companyName;

    @NotBlank(message = "Exchange must not be blank")
    @Size(min = 2, max = 20, message = "Exchange must be between 2 and 20 characters")
    private String exchange;

    @NotBlank(message = "Sector must not be blank")
    @Size(min = 2, max = 50,message = "Sector must be between 2 and 50 characters")
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
