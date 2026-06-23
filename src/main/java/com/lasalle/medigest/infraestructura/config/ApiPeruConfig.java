package com.lasalle.medigest.infraestructura.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * Configuración del cliente HTTP para la API de apiperu.dev.
 * Lee el token desde la variable de entorno APIPERU_TOKEN.
 */
@Configuration
public class ApiPeruConfig {

    @Value("${apiperu.api.base-url:https://apiperu.dev/api}")
    private String baseUrl;

    @Value("${apiperu.api.token:}")
    private String token;

    @Bean
    public RestTemplate apiPeruRestTemplate(RestTemplateBuilder builder) {
        return builder
                .rootUri(baseUrl)
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(10))
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String getToken() {
        return token;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
