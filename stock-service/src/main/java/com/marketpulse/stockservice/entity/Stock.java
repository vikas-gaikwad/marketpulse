package com.marketpulse.stockservice.entity;


import jakarta.persistence.*;

@Entity
@Table(
        name = "stocks",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_stock_symbol",
                        columnNames = "symbol"
                )
        }
)
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "symbol", length = 50, nullable = false, unique = true)
    private String symbol;

    @Column(name = "company_name", length = 255, nullable = false)
    private String companyName;

    @Column(name = "exchange", length = 50, nullable = false)
    private String exchange;

    @Column(name = "sector", length = 100)
    private String sector;

    public Stock() {
    }

    public Stock(String symbol,
                 String companyName,
                 String exchange,
                 String sector) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.exchange = exchange;
        this.sector = sector;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
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
