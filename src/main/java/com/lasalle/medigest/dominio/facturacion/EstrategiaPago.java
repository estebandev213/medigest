package com.lasalle.medigest.dominio.facturacion;

public interface EstrategiaPago {
    double calcularCobertura(double costoTotal);
    String getNombreEstrategia();
}
