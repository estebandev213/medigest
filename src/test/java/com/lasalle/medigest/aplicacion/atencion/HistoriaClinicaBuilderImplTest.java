package com.lasalle.medigest.aplicacion.atencion;

import com.lasalle.medigest.dominio.admision.Paciente;
import com.lasalle.medigest.dominio.atencion.HistoriaClinica;
import com.lasalle.medigest.dominio.citas.Cita;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class HistoriaClinicaBuilderImplTest {

    @Test
    void construir_armaLaHistoriaConTodosLosCamposYFechaDeHoy() {
        Paciente paciente = mock(Paciente.class);
        Cita cita = mock(Cita.class);

        HistoriaClinica historia = new HistoriaClinicaBuilderImpl(paciente, cita, "Dr. Pérez")
                .conDiagnostico("Migraña")
                .conAlergias("Ninguna")
                .conResultadosLaboratorio("Sin hallazgos")
                .conTratamiento("Paracetamol")
                .construir();

        assertThat(historia.getPaciente()).isEqualTo(paciente);
        assertThat(historia.getCita()).isEqualTo(cita);
        assertThat(historia.getMedicoTratante()).isEqualTo("Dr. Pérez");
        assertThat(historia.getDiagnostico()).isEqualTo("Migraña");
        assertThat(historia.getAlergias()).isEqualTo("Ninguna");
        assertThat(historia.getResultadosLaboratorio()).isEqualTo("Sin hallazgos");
        assertThat(historia.getTratamiento()).isEqualTo("Paracetamol");
        assertThat(historia.getFechaAtencion()).isEqualTo(LocalDate.now());
    }

    @Test
    void construir_permiteCitaNula_paraAtencionesSinCitaPrevia() {
        Paciente paciente = mock(Paciente.class);

        HistoriaClinica historia = new HistoriaClinicaBuilderImpl(paciente, null, "Dr. Gómez")
                .conDiagnostico("Control de rutina")
                .conTratamiento("Sin tratamiento")
                .construir();

        assertThat(historia.getCita()).isNull();
        assertThat(historia.getDiagnostico()).isEqualTo("Control de rutina");
    }
}
