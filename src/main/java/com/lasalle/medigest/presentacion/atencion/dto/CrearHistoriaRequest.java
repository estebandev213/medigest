package com.lasalle.medigest.presentacion.atencion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de entrada para RF06 — creación de historia clínica.
 * Reemplaza el Map<String, Object> genérico del controller original
 * para validar los datos antes de que lleguen al servicio.
 */
public record CrearHistoriaRequest(

        @NotNull(message = "pacienteId es obligatorio")
        Long pacienteId,

        Long citaId,

        @NotBlank(message = "medicoTratante es obligatorio")
        String medicoTratante,

        @NotBlank(message = "diagnostico es obligatorio")
        String diagnostico,

        String alergias,

        String resultadosLaboratorio,

        String tratamiento
) {
}
