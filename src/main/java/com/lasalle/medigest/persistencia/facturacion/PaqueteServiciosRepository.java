package com.lasalle.medigest.persistencia.facturacion;

import com.lasalle.medigest.dominio.facturacion.PaqueteServicios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaqueteServiciosRepository extends JpaRepository<PaqueteServicios, Long> {
}

