package com.lasalle.medigest.presentacion.citas;

import com.lasalle.medigest.aplicacion.citas.ServicioCitas;
import com.lasalle.medigest.dominio.citas.Cita;
import com.lasalle.medigest.dominio.citas.EstadoCita;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitasController {

    private final ServicioCitas servicioCitas;

    // RF03 — programar cita
    @PostMapping
    public ResponseEntity<Cita> programarCita(@RequestBody Cita cita) {
        return ResponseEntity.ok(servicioCitas.programarCita(cita));
    }

    // RF04 — confirmar cita (Observer notifica)
    @PutMapping("/{id}/confirmar")
    public ResponseEntity<Cita> confirmarCita(@PathVariable Long id) {
        return ResponseEntity.ok(servicioCitas.cambiarEstadoCita(id, EstadoCita.CONFIRMADA));
    }

    // RF05 — cancelar cita (Observer notifica)
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Cita> cancelarCita(@PathVariable Long id) {
        return ResponseEntity.ok(servicioCitas.cambiarEstadoCita(id, EstadoCita.CANCELADA));
    }

    // RF05 — reprogramar = cambio de estado genérico
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Cita> cambiarEstado(@PathVariable Long id,
                                               @RequestParam EstadoCita nuevoEstado) {
        return ResponseEntity.ok(servicioCitas.cambiarEstadoCita(id, nuevoEstado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> buscarPorId(@PathVariable Long id) {
        return servicioCitas.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Cita>> listarTodas() {
        return ResponseEntity.ok(servicioCitas.listarTodas());
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<Cita>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(servicioCitas.listarPorPaciente(pacienteId));
    }
}
