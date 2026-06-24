package com.lasalle.medigest.dominio.facturacion;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("PAQUETE")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, exclude = "componentes")
public class PaqueteServicios extends ItemFacturable {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "paquete_id", nullable = true)
    private List<ItemFacturable> componentes = new ArrayList<>();

    public PaqueteServicios(String nombre) {
        super(null, nombre);
    }

    public void agregar(ItemFacturable item) {
        componentes.add(item);
    }

    public void eliminar(ItemFacturable item) {
        componentes.remove(item);
    }

    @Override
    public double calcularCosto() {
        return componentes.stream()
                .mapToDouble(ServicioFacturable::calcularCosto)
                .sum();
    }
}
