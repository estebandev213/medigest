package com.lasalle.medigest.aplicacion.facturacion;

import com.lasalle.medigest.dominio.facturacion.EstrategiaPago;
import com.lasalle.medigest.dominio.facturacion.ServicioFacturable;

public class CalculadorFactura {

    private EstrategiaPago estrategiaPago;

    public CalculadorFactura(EstrategiaPago estrategiaPago) {
        this.estrategiaPago = estrategiaPago;
    }

    public void setEstrategiaPago(EstrategiaPago estrategiaPago) {
        this.estrategiaPago = estrategiaPago;
    }

    public ResultadoFactura calcular(ServicioFacturable servicio) {
        double costoTotal    = servicio.calcularCosto();
        double montoCubierto = estrategiaPago.calcularCobertura(costoTotal);
        double montoPaciente = costoTotal - montoCubierto;
        return new ResultadoFactura(costoTotal, montoCubierto, montoPaciente, estrategiaPago.getNombreEstrategia());
    }

    public record ResultadoFactura(
            double costoTotal,
            double montoCubierto,
            double montoPaciente,
            String tipoCobertura
    ) {}
}
