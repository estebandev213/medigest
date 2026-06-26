package com.lasalle.medigest.presentacion.atencion.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO de entrada para el PATCH de resultados de laboratorio.
 */
public record AgregarResultadosRequest(

        @NotBlank(message = "resultados es obligatorio")
        String resultados
) {
}
