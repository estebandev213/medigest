package com.lasalle.medigest.aplicacion.facturacion;

import com.lasalle.medigest.dominio.facturacion.EstrategiaPago;
import org.springframework.stereotype.Component;

@Component
public class PagoParticular implements EstrategiaPago {

    @Override
    public double calcularCobertura(double costoTotal) {
        return 0.0;
    }

    @Override
    public String getNombreEstrategia() {
        return "PARTICULAR";
    }
}
