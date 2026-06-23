package com.lasalle.medigest.aplicacion.admision;

import com.lasalle.medigest.dominio.admision.DatosPaciente;
import com.lasalle.medigest.dominio.admision.Paciente;
import com.lasalle.medigest.dominio.admision.ValidadorIdentidad;
import com.lasalle.medigest.persistencia.admision.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicioAdmision {

    private final PacienteRepository pacienteRepository;
    private final ValidadorIdentidad validadorIdentidad;

    public Paciente registrarPaciente(Paciente paciente) {
        if (pacienteRepository.findByDni(paciente.getDni()).isPresent()) {
            throw new IllegalStateException("Ya existe un paciente con DNI: " + paciente.getDni());
        }
        return pacienteRepository.save(paciente);
    }

    public DatosPaciente validarIdentidad(String dni) {
        return validadorIdentidad.validar(dni);
    }

    public Optional<Paciente> buscarPorDni(String dni) {
        return pacienteRepository.findByDni(dni);
    }

    public Optional<Paciente> buscarPorId(Long id) {
        return pacienteRepository.findById(id);
    }

    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }
}
