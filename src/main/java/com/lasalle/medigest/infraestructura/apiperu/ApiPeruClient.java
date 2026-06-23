package com.lasalle.medigest.infraestructura.apiperu;

import com.lasalle.medigest.infraestructura.config.ApiPeruConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Cliente HTTP que encapsula las llamadas a la API de apiperu.dev.
 * Maneja autenticación (Bearer token), serialización y errores de red.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApiPeruClient {

    private final RestTemplate apiPeruRestTemplate;
    private final ApiPeruConfig apiPeruConfig;

    /**
     * Consulta los datos de una persona por su DNI.
     *
     * @param dni Número de DNI de 8 dígitos
     * @return respuesta de la API con los datos del titular
     * @throws ApiPeruException si la API no responde o devuelve error
     */
    public ApiPeruDniResponse consultarDni(String dni) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiPeruConfig.getToken());

        Map<String, String> body = Map.of("dni", dni);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            log.info("Consultando DNI {} en apiperu.dev", dni);

            ResponseEntity<ApiPeruDniResponse> response = apiPeruRestTemplate.exchange(
                    "/dni",
                    HttpMethod.POST,
                    request,
                    ApiPeruDniResponse.class
            );

            if (response.getBody() == null) {
                throw new ApiPeruException("Respuesta vacía de apiperu.dev para DNI: " + dni);
            }

            log.info("Respuesta recibida para DNI {}: success={}", dni, response.getBody().isSuccess());
            return response.getBody();

        } catch (RestClientException e) {
            log.error("Error al consultar DNI {} en apiperu.dev: {}", dni, e.getMessage());
            throw new ApiPeruException("Error al consultar API Peru: " + e.getMessage(), e);
        }
    }
}
