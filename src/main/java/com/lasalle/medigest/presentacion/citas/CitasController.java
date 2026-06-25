package com.lasalle.medigest.presentacion.citas;

import com.lasalle.medigest.aplicacion.citas.ServicioCitas;
import com.lasalle.medigest.dominio.citas.Cita;
import com.lasalle.medigest.dominio.citas.EstadoCita;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        return new ResponseEntity<>(servicioCitas.programarCita(cita), HttpStatus.CREATED);
    }

    // RF04 — confirmar cita (Usa el método específico del servicio)
    @PutMapping("/{id}/confirmar")
    public ResponseEntity<Cita> confirmarCita(@PathVariable Long id) {
        return ResponseEntity.ok(servicioCitas.confirmarCita(id));
    }

    // RF05 — cancelar cita (Usa el método específico del servicio)
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Cita> cancelarCita(@PathVariable Long id) {
        return ResponseEntity.ok(servicioCitas.cancelarCita(id));
    }

    // RF05 — reprogramar = cambio de estado genérico
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Cita> cambiarEstado(@PathVariable Long id,
                                               @RequestParam EstadoCita nuevoEstado) {
        return ResponseEntity.ok(servicioCitas.cambiarEstadoGenerico(id, nuevoEstado));
    }

    // Buscar por ID corregido (Cambiamos .map() por una validación limpia de null)
    @GetMapping("/{id}")
    public ResponseEntity<Cita> buscarPorId(@PathVariable Long id) {
        Cita cita = servicioCitas.buscarPorId(id);
        return cita != null ? ResponseEntity.ok(cita) : ResponseEntity.notFound().build();
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