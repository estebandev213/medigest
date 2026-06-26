package com.lasalle.medigest.presentacion.atencion.dto;

import com.lasalle.medigest.dominio.atencion.HistoriaClinica;

import java.time.LocalDate;

/**
 * DTO de salida. Evita exponer la entidad JPA directamente, lo cual
 * previene recursión infinita en la serialización si Paciente/Cita
 * llegaran a referenciar de vuelta a sus historias, y evita filtrar
 * campos internos de la entidad.
 *
 * Si necesitan más datos del paciente (nombre, documento, etc.) agreguen
 * los campos aquí según los getters reales de la clase Paciente.
 */
public record HistoriaClinicaResponse(
        Long id,
        Long pacienteId,
        Long citaId,
        LocalDate fechaAtencion,
        String medicoTratante,
        String diagnostico,
        String alergias,
        String resultadosLaboratorio,
        String tratamiento
) {

    public static HistoriaClinicaResponse desde(HistoriaClinica historia) {
        return new HistoriaClinicaResponse(
                historia.getId(),
                historia.getPaciente() != null ? historia.getPaciente().getId() : null,
                historia.getCita() != null ? historia.getCita().getId() : null,
                historia.getFechaAtencion(),
                historia.getMedicoTratante(),
                historia.getDiagnostico(),
                historia.getAlergias(),
                historia.getResultadosLaboratorio(),
                historia.getTratamiento()
        );
    }
}
