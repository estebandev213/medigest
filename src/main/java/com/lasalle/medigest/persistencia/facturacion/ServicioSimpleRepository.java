package com.lasalle.medigest.persistencia.facturacion;

import com.lasalle.medigest.dominio.facturacion.ServicioSimple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioSimpleRepository extends JpaRepository<ServicioSimple, Long> {
}

