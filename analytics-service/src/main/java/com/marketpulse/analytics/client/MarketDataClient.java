package com.marketpulse.analytics.client;

import com.marketpulse.analytics.dto.MarketDataResponse;
import com.marketpulse.analytics.exception.MarketDataServiceUnavailableException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.List;

@Component
public class MarketDataClient {

    private final RestClient restClient;

    public MarketDataClient(RestClient marketDataRestClient) {
        this.restClient = marketDataRestClient;
    }

    public List<MarketDataResponse> getHistoricalData(
            String symbol,
            LocalDate from,
            LocalDate to) {

        try {
            MarketDataResponse[] response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/market-data/{symbol}/history")
                            .queryParam("from", from)
                            .queryParam("to", to)
                            .build(symbol))
                    .retrieve()
                    .body(MarketDataResponse[].class);

            return response == null ? List.of() : List.of(response);

        } catch (ResourceAccessException exception) {
            throw new MarketDataServiceUnavailableException(
                    "Unable to connect to Market Data Service",
                    exception);
        }
    }
}
