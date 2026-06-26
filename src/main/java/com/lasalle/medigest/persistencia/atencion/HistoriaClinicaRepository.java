package com.lasalle.medigest.persistencia.atencion;

import com.lasalle.medigest.dominio.atencion.HistoriaClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Long> {
    List<HistoriaClinica> findByPacienteId(Long pacienteId);

    Optional<HistoriaClinica> findByCitaId(Long citaId);
}
