package com.lasalle.medigest.dominio.facturacion;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "items_facturables")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_item", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public abstract class ItemFacturable implements ServicioFacturable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Override
    public String getNombre() {
        return nombre;
    }
}
