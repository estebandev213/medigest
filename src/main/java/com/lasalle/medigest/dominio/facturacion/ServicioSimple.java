package com.lasalle.medigest.dominio.facturacion;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("SIMPLE")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class ServicioSimple extends ItemFacturable {

    @Column
    private double precioBase;

    public ServicioSimple(String nombre, double precioBase) {
        super(null, nombre);
        this.precioBase = precioBase;
    }

    @Override
    public double calcularCosto() {
        return precioBase;
    }
}
