package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ExternalApiClientConfig {
    @Value("${external-api-url}")
    private String apiBaseUrl;

    @Bean
    public RestClient restClient() {
        return RestClient.create(apiBaseUrl);
    }
}
