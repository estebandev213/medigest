package com.lasalle.medigest.presentacion.atencion;

import com.lasalle.medigest.aplicacion.atencion.ServicioAtencion;
import com.lasalle.medigest.dominio.atencion.HistoriaClinica;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/atencion")
@RequiredArgsConstructor
public class AtencionController {

    private final ServicioAtencion servicioAtencion;

    // RF06 — crear historia clínica (Builder + Director)
    @PostMapping("/historias")
    public ResponseEntity<HistoriaClinica> crearHistoria(@RequestBody Map<String, Object> req) {
        Long pacienteId = Long.valueOf(req.get("pacienteId").toString());
        Long citaId     = req.get("citaId") != null ? Long.valueOf(req.get("citaId").toString()) : null;
        String medico       = (String) req.get("medicoTratante");
        String diagnostico  = (String) req.get("diagnostico");
        String alergias     = (String) req.getOrDefault("alergias", "");
        String resultados   = (String) req.getOrDefault("resultadosLaboratorio", "");
        String tratamiento  = (String) req.getOrDefault("tratamiento", "");

        return ResponseEntity.ok(
            servicioAtencion.crearHistoriaClinica(pacienteId, citaId, medico, diagnostico, alergias, resultados, tratamiento)
        );
    }

    // agregar resultados de laboratorio
    @PatchMapping("/historias/{id}/laboratorio")
    public ResponseEntity<HistoriaClinica> agregarResultados(
            @PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(servicioAtencion.agregarResultadosLaboratorio(id, body.get("resultados")));
    }

    // RF07 — consultar historia clínica
    @GetMapping("/historias/{id}")
    public ResponseEntity<HistoriaClinica> buscarPorId(@PathVariable Long id) {
        return servicioAtencion.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/historias/paciente/{pacienteId}")
    public ResponseEntity<List<HistoriaClinica>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(servicioAtencion.listarPorPaciente(pacienteId));
    }
}
