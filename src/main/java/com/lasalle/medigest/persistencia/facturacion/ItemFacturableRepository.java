package com.lasalle.medigest.persistencia.facturacion;

import com.lasalle.medigest.dominio.facturacion.ItemFacturable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemFacturableRepository extends JpaRepository<ItemFacturable, Long> {
}
