package com.marketpulse.analytics.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient marketDataRestClient(
            @Value("${market-data.service.base-url}") String baseUrl) {

        return RestClient.create(baseUrl);
    }
}
