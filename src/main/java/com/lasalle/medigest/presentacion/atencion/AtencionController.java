package com.lasalle.medigest.presentacion.atencion;

import com.lasalle.medigest.aplicacion.atencion.ServicioAtencion;
import com.lasalle.medigest.presentacion.atencion.dto.AgregarResultadosRequest;
import com.lasalle.medigest.presentacion.atencion.dto.CrearHistoriaRequest;
import com.lasalle.medigest.presentacion.atencion.dto.HistoriaClinicaResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/atencion")
@RequiredArgsConstructor
public class AtencionController {

    private final ServicioAtencion servicioAtencion;

    // RF06 — crear historia clínica (Builder + Director)
    @PostMapping("/historias")
    public ResponseEntity<HistoriaClinicaResponse> crearHistoria(@Valid @RequestBody CrearHistoriaRequest req) {
        var historia = servicioAtencion.crearHistoriaClinica(
                req.pacienteId(),
                req.citaId(),
                req.medicoTratante(),
                req.diagnostico(),
                req.alergias(),
                req.resultadosLaboratorio(),
                req.tratamiento()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(HistoriaClinicaResponse.desde(historia));
    }

    // agregar resultados de laboratorio
    @PatchMapping("/historias/{id}/laboratorio")
    public ResponseEntity<HistoriaClinicaResponse> agregarResultados(
            @PathVariable Long id, @Valid @RequestBody AgregarResultadosRequest body) {
        var historia = servicioAtencion.agregarResultadosLaboratorio(id, body.resultados());
        return ResponseEntity.ok(HistoriaClinicaResponse.desde(historia));
    }

    // RF07 — consultar historia clínica
    @GetMapping("/historias/{id}")
    public ResponseEntity<HistoriaClinicaResponse> buscarPorId(@PathVariable Long id) {
        return servicioAtencion.buscarPorId(id)
                .map(HistoriaClinicaResponse::desde)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/historias/paciente/{pacienteId}")
    public ResponseEntity<List<HistoriaClinicaResponse>> listarPorPaciente(@PathVariable Long pacienteId) {
        var historias = servicioAtencion.listarPorPaciente(pacienteId).stream()
                .map(HistoriaClinicaResponse::desde)
                .toList();
        return ResponseEntity.ok(historias);
    }
}
