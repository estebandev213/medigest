package com.lasalle.medigest.dominio.atencion;

import com.lasalle.medigest.dominio.admision.Paciente;
import com.lasalle.medigest.dominio.citas.Cita;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "historias_clinicas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"paciente", "cita"})
public class HistoriaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cita_id")
    private Cita cita;

    private LocalDate fechaAtencion;
    private String medicoTratante;

    @Column(length = 2000)
    private String diagnostico;

    @Column(length = 1000)
    private String alergias;

    @Column(length = 2000)
    private String resultadosLaboratorio;

    @Column(length = 2000)
    private String tratamiento;
}
