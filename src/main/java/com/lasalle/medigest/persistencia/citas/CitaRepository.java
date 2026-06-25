package com.lasalle.medigest.persistencia.citas;

import com.lasalle.medigest.dominio.citas.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    
    // Método clave para el endpoint: Listar citas de un paciente
    List<Cita> findByPacienteId(Long pacienteId);
}