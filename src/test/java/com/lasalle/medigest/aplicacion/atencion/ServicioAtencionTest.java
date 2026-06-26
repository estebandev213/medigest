package com.lasalle.medigest.aplicacion.atencion;

import com.lasalle.medigest.dominio.admision.Paciente;
import com.lasalle.medigest.dominio.atencion.HistoriaClinica;
import com.lasalle.medigest.dominio.citas.Cita;
import com.lasalle.medigest.dominio.excepcion.RecursoNoEncontradoException;
import com.lasalle.medigest.persistencia.admision.PacienteRepository;
import com.lasalle.medigest.persistencia.atencion.HistoriaClinicaRepository;
import com.lasalle.medigest.persistencia.citas.CitaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServicioAtencionTest {

    @Mock
    private HistoriaClinicaRepository historiaClinicaRepository;
    @Mock
    private PacienteRepository pacienteRepository;
    @Mock
    private CitaRepository citaRepository;

    @InjectMocks
    private ServicioAtencion servicioAtencion;

    private Paciente paciente;

    @BeforeEach
    void setUp() {
        paciente = mock(Paciente.class);
    }

    @Test
    void crearHistoriaClinica_construyeYGuardaConPacienteYCitaValidos() {
        Cita cita = mock(Cita.class);
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(citaRepository.findById(10L)).thenReturn(Optional.of(cita));
        when(historiaClinicaRepository.save(any(HistoriaClinica.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        HistoriaClinica resultado = servicioAtencion.crearHistoriaClinica(
                1L, 10L, "Dr. Ruiz", "Gripe", "Ninguna", "Normal", "Reposo");

        assertThat(resultado.getDiagnostico()).isEqualTo("Gripe");
        assertThat(resultado.getPaciente()).isEqualTo(paciente);
        assertThat(resultado.getCita()).isEqualTo(cita);
        verify(historiaClinicaRepository).save(any(HistoriaClinica.class));
    }

    @Test
    void crearHistoriaClinica_permiteCitaIdNuloSinConsultarCitaRepository() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(historiaClinicaRepository.save(any(HistoriaClinica.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        HistoriaClinica resultado = servicioAtencion.crearHistoriaClinica(
                1L, null, "Dr. Ruiz", "Control", "Ninguna", "", "");

        assertThat(resultado.getCita()).isNull();
        verify(citaRepository, never()).findById(any());
    }

    @Test
    void crearHistoriaClinica_lanzaRecursoNoEncontradoSiNoExistePaciente() {
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> servicioAtencion.crearHistoriaClinica(
                99L, null, "Dr. Ruiz", "Gripe", "", "", ""))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("99");

        verify(historiaClinicaRepository, never()).save(any());
    }

    @Test
    void agregarResultadosLaboratorio_actualizaYGuardaLaHistoria() {
        HistoriaClinica historia = HistoriaClinica.builder().id(5L).build();
        when(historiaClinicaRepository.findById(5L)).thenReturn(Optional.of(historia));
        when(historiaClinicaRepository.save(any(HistoriaClinica.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        HistoriaClinica resultado = servicioAtencion.agregarResultadosLaboratorio(5L, "Hemograma alterado");

        assertThat(resultado.getResultadosLaboratorio()).isEqualTo("Hemograma alterado");
    }

    @Test
    void agregarResultadosLaboratorio_lanzaRecursoNoEncontradoSiNoExisteHistoria() {
        when(historiaClinicaRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> servicioAtencion.agregarResultadosLaboratorio(404L, "x"))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }

    @Test
    void buscarPorId_devuelveOptionalVacioSiNoExiste() {
        when(historiaClinicaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThat(servicioAtencion.buscarPorId(1L)).isEmpty();
    }

    @Test
    void listarPorPaciente_delegaDirectamenteAlRepositorio() {
        List<HistoriaClinica> esperado = List.of(HistoriaClinica.builder().id(1L).build());
        when(historiaClinicaRepository.findByPacienteId(7L)).thenReturn(esperado);

        assertThat(servicioAtencion.listarPorPaciente(7L)).isEqualTo(esperado);
    }
}
