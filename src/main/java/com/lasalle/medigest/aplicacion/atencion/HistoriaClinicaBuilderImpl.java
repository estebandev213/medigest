package com.lasalle.medigest.aplicacion.atencion;

import com.lasalle.medigest.dominio.atencion.HistoriaClinica;
import com.lasalle.medigest.dominio.atencion.HistoriaClinicaBuilder;
import com.lasalle.medigest.dominio.admision.Paciente;
import com.lasalle.medigest.dominio.citas.Cita;

import java.time.LocalDate;

public class HistoriaClinicaBuilderImpl implements HistoriaClinicaBuilder {

    private final Paciente paciente;
    private final Cita cita;
    private final String medicoTratante;
    private String diagnostico;
    private String alergias;
    private String resultadosLaboratorio;
    private String tratamiento;

    public HistoriaClinicaBuilderImpl(Paciente paciente, Cita cita, String medicoTratante) {
        this.paciente = paciente;
        this.cita = cita;
        this.medicoTratante = medicoTratante;
    }

    @Override
    public HistoriaClinicaBuilder conDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
        return this;
    }

    @Override
    public HistoriaClinicaBuilder conAlergias(String alergias) {
        this.alergias = alergias;
        return this;
    }

    @Override
    public HistoriaClinicaBuilder conResultadosLaboratorio(String resultados) {
        this.resultadosLaboratorio = resultados;
        return this;
    }

    @Override
    public HistoriaClinicaBuilder conTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
        return this;
    }

    @Override
    public HistoriaClinica construir() {
        return HistoriaClinica.builder()
                .paciente(paciente)
                .cita(cita)
                .medicoTratante(medicoTratante)
                .fechaAtencion(LocalDate.now())
                .diagnostico(diagnostico)
                .alergias(alergias)
                .resultadosLaboratorio(resultadosLaboratorio)
                .tratamiento(tratamiento)
                .build();
    }
}
