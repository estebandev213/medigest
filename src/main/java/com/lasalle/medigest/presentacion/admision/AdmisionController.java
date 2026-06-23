package com.lasalle.medigest.presentacion.admision;

import com.lasalle.medigest.aplicacion.admision.ServicioAdmision;
import com.lasalle.medigest.dominio.admision.DatosPaciente;
import com.lasalle.medigest.dominio.admision.Paciente;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admision")
@RequiredArgsConstructor
public class AdmisionController {

    private final ServicioAdmision servicioAdmision;

    // RF01 — registrar paciente
    @PostMapping("/pacientes")
    public ResponseEntity<Paciente> registrarPaciente(@RequestBody Paciente paciente) {
        return ResponseEntity.ok(servicioAdmision.registrarPaciente(paciente));
    }

    // RF02 — validar identidad vía RENIEC (Adapter)
    @GetMapping("/pacientes/{dni}/validar")
    public ResponseEntity<DatosPaciente> validarIdentidad(@PathVariable String dni) {
        return ResponseEntity.ok(servicioAdmision.validarIdentidad(dni));
    }

    // UC03 — buscar paciente por DNI
    @GetMapping("/pacientes/dni/{dni}")
    public ResponseEntity<Paciente> buscarPorDni(@PathVariable String dni) {
        return servicioAdmision.buscarPorDni(dni)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pacientes/{id}")
    public ResponseEntity<Paciente> buscarPorId(@PathVariable Long id) {
        return servicioAdmision.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pacientes")
    public ResponseEntity<List<Paciente>> listarPacientes() {
        return ResponseEntity.ok(servicioAdmision.listarTodos());
    }
}
