package com.lasalle.medigest.aplicacion.atencion;

import com.lasalle.medigest.dominio.admision.Paciente;
import com.lasalle.medigest.dominio.atencion.HistoriaClinica;
import com.lasalle.medigest.dominio.citas.Cita;
import com.lasalle.medigest.dominio.excepcion.RecursoNoEncontradoException;
import com.lasalle.medigest.persistencia.admision.PacienteRepository;
import com.lasalle.medigest.persistencia.atencion.HistoriaClinicaRepository;
import com.lasalle.medigest.persistencia.citas.CitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServicioAtencion {

    private final HistoriaClinicaRepository historiaClinicaRepository;
    private final PacienteRepository pacienteRepository;
    private final CitaRepository citaRepository;

    public HistoriaClinica crearHistoriaClinica(Long pacienteId, Long citaId, String medicoTratante,
                                                 String diagnostico, String alergias,
                                                 String resultados, String tratamiento) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado: " + pacienteId));

        Cita cita = null;
        if (citaId != null) {
            cita = citaRepository.findById(citaId)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Cita no encontrada: " + citaId));
            if (!cita.getPaciente().getId().equals(pacienteId)) {
                throw new IllegalArgumentException(
                        "La cita #" + citaId + " no pertenece al paciente seleccionado.");
            }
            if (historiaClinicaRepository.findByCitaId(citaId).isPresent()) {
                throw new IllegalStateException(
                        "La cita #" + citaId + " ya tiene una historia clínica. Elija otra cita o déjela sin vincular.");
            }
        }

        DirectorAtencion director = new DirectorAtencion(
                new HistoriaClinicaBuilderImpl(paciente, cita, medicoTratante));
        HistoriaClinica historia = director.construirHistoriaCompleta(diagnostico, alergias, resultados, tratamiento);
        return historiaClinicaRepository.save(historia);
    }

    public HistoriaClinica agregarResultadosLaboratorio(Long historiaId, String resultados) {
        HistoriaClinica historia = historiaClinicaRepository.findById(historiaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Historia clínica no encontrada: " + historiaId));
        historia.setResultadosLaboratorio(resultados);
        return historiaClinicaRepository.save(historia);
    }

    public Optional<HistoriaClinica> buscarPorId(Long id) {
        return historiaClinicaRepository.findById(id);
    }

    public List<HistoriaClinica> listarPorPaciente(Long pacienteId) {
        return historiaClinicaRepository.findByPacienteId(pacienteId);
    }

    public List<HistoriaClinica> listarTodas() {
        return historiaClinicaRepository.findAll();
    }

    public Set<Long> listarCitaIdsConHistoria() {
        return historiaClinicaRepository.findAll().stream()
                .filter(h -> h.getCita() != null)
                .map(h -> h.getCita().getId())
                .collect(Collectors.toSet());
    }
}
