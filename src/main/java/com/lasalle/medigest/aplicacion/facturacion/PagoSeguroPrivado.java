package com.lasalle.medigest.aplicacion.facturacion;

import com.lasalle.medigest.dominio.facturacion.EstrategiaPago;
import org.springframework.stereotype.Component;

@Component
public class PagoSeguroPrivado implements EstrategiaPago {

    private static final double COBERTURA = 0.60;

    @Override
    public double calcularCobertura(double costoTotal) {
        return costoTotal * COBERTURA;
    }

    @Override
    public String getNombreEstrategia() {
        return "SEGURO_PRIVADO";
    }
}
