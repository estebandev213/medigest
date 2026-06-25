package com.lasalle.medigest.dominio.citas;

import com.lasalle.medigest.dominio.admision.Paciente;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "citas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"paciente", "observadores"})
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Column(nullable = false)
    private String medicoAsignado;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    private String especialidad;
    private String motivo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EstadoCita estado = EstadoCita.PENDIENTE;

    @Transient
    @JsonIgnore
    private List<Observer> observadores;

    public void agregarObservador(Observer obs) {
        if (observadores == null) {
            observadores = new ArrayList<>();
        }
        observadores.add(obs);
    }

    public void cambiarEstado(EstadoCita nuevoEstado) {
        this.estado = nuevoEstado;
        notificar();
    }

    private void notificar() {
        if (observadores != null) {
            observadores.forEach(obs -> obs.actualizar(this));
        }
    }
}