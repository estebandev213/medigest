package com.lasalle.medigest.aplicacion.atencion;

import com.lasalle.medigest.dominio.admision.Paciente;
import com.lasalle.medigest.dominio.atencion.HistoriaClinica;
import com.lasalle.medigest.dominio.citas.Cita;
import com.lasalle.medigest.persistencia.admision.PacienteRepository;
import com.lasalle.medigest.persistencia.atencion.HistoriaClinicaRepository;
import com.lasalle.medigest.persistencia.citas.CitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado: " + pacienteId));
        Cita cita = citaId != null ? citaRepository.findById(citaId).orElse(null) : null;

        DirectorAtencion director = new DirectorAtencion(
                new HistoriaClinicaBuilderImpl(paciente, cita, medicoTratante));
        HistoriaClinica historia = director.construirHistoriaCompleta(diagnostico, alergias, resultados, tratamiento);
        return historiaClinicaRepository.save(historia);
    }

    public HistoriaClinica agregarResultadosLaboratorio(Long historiaId, String resultados) {
        HistoriaClinica historia = historiaClinicaRepository.findById(historiaId)
                .orElseThrow(() -> new IllegalArgumentException("Historia clínica no encontrada: " + historiaId));
        historia.setResultadosLaboratorio(resultados);
        return historiaClinicaRepository.save(historia);
    }

    public Optional<HistoriaClinica> buscarPorId(Long id) {
        return historiaClinicaRepository.findById(id);
    }

    public List<HistoriaClinica> listarPorPaciente(Long pacienteId) {
        return historiaClinicaRepository.findByPacienteId(pacienteId);
    }
}
