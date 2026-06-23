package com.lasalle.medigest.infraestructura.apiperu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que mapea la respuesta JSON de la API POST /api/dni de apiperu.dev.
 *
 * Ejemplo de respuesta:
 * {
 *   "success": true,
 *   "data": {
 *     "numero": "12345678",
 *     "nombre_completo": "PÉREZ AGUILAR, CATALINA",
 *     "nombres": "CATALINA",
 *     "apellido_paterno": "PÉREZ",
 *     "apellido_materno": "AGUILAR",
 *     "codigo_verificacion": "8"
 *   }
 * }
 */
@Data
@NoArgsConstructor
public class ApiPeruDniResponse {

    private boolean success;
    private DniData data;

    @Data
    @NoArgsConstructor
    public static class DniData {

        private String numero;

        @JsonProperty("nombre_completo")
        private String nombreCompleto;

        private String nombres;

        @JsonProperty("apellido_paterno")
        private String apellidoPaterno;

        @JsonProperty("apellido_materno")
        private String apellidoMaterno;

        @JsonProperty("codigo_verificacion")
        private String codigoVerificacion;
    }
}
