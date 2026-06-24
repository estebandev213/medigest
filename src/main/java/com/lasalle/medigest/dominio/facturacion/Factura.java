package com.lasalle.medigest.dominio.facturacion;

import com.lasalle.medigest.dominio.admision.Paciente;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "facturas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "paciente")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    /** Referencia al ítem facturado (ServicioSimple o PaqueteServicios). */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private ItemFacturable item;

    private LocalDateTime fechaEmision;
    private double costoTotal;
    private double montoCubierto;
    private double montoPaciente;

    @Enumerated(EnumType.STRING)
    private TipoCobertura tipoCobertura;

    @Column(length = 500)
    private String detalle;
}
