package com.lasalle.medigest.aplicacion.citas;

import com.lasalle.medigest.dominio.citas.Cita;
import com.lasalle.medigest.dominio.citas.EstadoCita;
import com.lasalle.medigest.dominio.citas.Observer;
import com.lasalle.medigest.persistencia.citas.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioCitas {

    @Autowired
    private CitaRepository citaRepository;

    // Spring busca todos los @Component que implementen Observer e inyecta la lista aquí
    @Autowired
    private List<Observer> listadoObservers;

    // Vincula los observadores administrados por Spring a la entidad antes de modificarla
    private void acoplarObservadores(Cita cita) {
        if (listadoObservers != null) {
            listadoObservers.forEach(cita::agregarObservador);
        }
    }

    // RF03 — Programar cita
    public Cita programarCita(Cita cita) {
        acoplarObservadores(cita);
        cita.cambiarEstado(EstadoCita.PENDIENTE); 
        return citaRepository.save(cita);
    }

    // RF04 — Confirmar cita
    public Cita confirmarCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Cita no encontrada."));
        acoplarObservadores(cita);
        cita.cambiarEstado(EstadoCita.CONFIRMADA);
        return citaRepository.save(cita);
    }

    // RF05 — Cancelar cita
    public Cita cancelarCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Cita no encontrada."));
        acoplarObservadores(cita);
        cita.cambiarEstado(EstadoCita.CANCELADA);
        return citaRepository.save(cita);
    }

    // PATCH — Cambio de estado genérico (por ejemplo: pasar a ATENDIDA)
    public Cita cambiarEstadoGenerico(Long id, EstadoCita nuevoEstado) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Cita no encontrada."));
        acoplarObservadores(cita);
        cita.cambiarEstado(nuevoEstado);
        return citaRepository.save(cita);
    }

    public Cita buscarPorId(Long id) {
        return citaRepository.findById(id).orElse(null);
    }

    public List<Cita> listarTodas() {
        return citaRepository.findAll();
    }

    public List<Cita> listarPorPaciente(Long pacienteId) {
        return citaRepository.findByPacienteId(pacienteId);
    }
}