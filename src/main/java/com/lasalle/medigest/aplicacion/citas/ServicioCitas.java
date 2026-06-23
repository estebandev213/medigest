package com.lasalle.medigest.aplicacion.citas;

import com.lasalle.medigest.dominio.citas.Cita;
import com.lasalle.medigest.dominio.citas.EstadoCita;
import com.lasalle.medigest.persistencia.citas.CitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicioCitas {

    private final CitaRepository citaRepository;
    private final NotificadorMedico notificadorMedico;
    private final RegistroAuditoria registroAuditoria;

    public Cita programarCita(Cita cita) {
        cita.setEstado(EstadoCita.PENDIENTE);
        return citaRepository.save(cita);
    }

    public Cita cambiarEstadoCita(Long id, EstadoCita nuevoEstado) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada: " + id));
        cita.agregarObservador(notificadorMedico);
        cita.agregarObservador(registroAuditoria);
        cita.cambiarEstado(nuevoEstado);
        return citaRepository.save(cita);
    }

    public Optional<Cita> buscarPorId(Long id) {
        return citaRepository.findById(id);
    }

    public List<Cita> listarTodas() {
        return citaRepository.findAll();
    }

    public List<Cita> listarPorPaciente(Long pacienteId) {
        return citaRepository.findByPacienteId(pacienteId);
    }
}
