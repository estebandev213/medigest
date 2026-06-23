package com.lasalle.medigest.persistencia.citas;

import com.lasalle.medigest.dominio.citas.Cita;
import com.lasalle.medigest.dominio.citas.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByPacienteId(Long pacienteId);
    List<Cita> findByEstado(EstadoCita estado);
    List<Cita> findByMedicoAsignado(String medicoAsignado);
}
