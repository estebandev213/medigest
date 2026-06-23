package com.lasalle.medigest.persistencia.facturacion;

import com.lasalle.medigest.dominio.facturacion.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    List<Factura> findByPacienteId(Long pacienteId);
}
