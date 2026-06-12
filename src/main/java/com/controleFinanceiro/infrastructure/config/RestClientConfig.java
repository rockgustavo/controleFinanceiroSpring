package com.controleFinanceiro.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${brapi.url:https://brapi.dev}")
    private String brapiUrl;

    @Bean("brapiRestClient")
    RestClient brapiRestClient(RestClient.Builder builder) {
        return builder.baseUrl(brapiUrl).build();
    }
}
