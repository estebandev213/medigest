package com.lasalle.medigest.aplicacion.atencion;

import com.lasalle.medigest.dominio.atencion.HistoriaClinica;
import com.lasalle.medigest.dominio.atencion.HistoriaClinicaBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DirectorAtencionTest {

    @Test
    void construirHistoriaCompleta_invocaTodosLosPasosDelBuilderEnOrden() {
        // RETURNS_SELF simula el encadenamiento fluido (conX().conY()...) sin
        // necesidad de implementar un fake completo del builder.
        HistoriaClinicaBuilder builder = mock(HistoriaClinicaBuilder.class, Mockito.RETURNS_SELF);
        HistoriaClinica esperado = HistoriaClinica.builder().diagnostico("Gripe").build();
        when(builder.construir()).thenReturn(esperado);

        DirectorAtencion director = new DirectorAtencion(builder);
        HistoriaClinica resultado = director.construirHistoriaCompleta(
                "Gripe", "Penicilina", "Hemograma normal", "Reposo");

        verify(builder).conDiagnostico("Gripe");
        verify(builder).conAlergias("Penicilina");
        verify(builder).conResultadosLaboratorio("Hemograma normal");
        verify(builder).conTratamiento("Reposo");
        verify(builder).construir();
        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    void construirHistoriaBasica_soloInvocaDiagnosticoYTratamiento() {
        HistoriaClinicaBuilder builder = mock(HistoriaClinicaBuilder.class, Mockito.RETURNS_SELF);
        HistoriaClinica esperado = HistoriaClinica.builder().diagnostico("Control").build();
        when(builder.construir()).thenReturn(esperado);

        DirectorAtencion director = new DirectorAtencion(builder);
        HistoriaClinica resultado = director.construirHistoriaBasica("Control", "Ninguno");

        verify(builder).conDiagnostico("Control");
        verify(builder).conTratamiento("Ninguno");
        verify(builder, never()).conAlergias(any());
        verify(builder, never()).conResultadosLaboratorio(any());
        assertThat(resultado).isEqualTo(esperado);
    }
}
