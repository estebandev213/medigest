package com.lasalle.medigest.aplicacion.citas;

import com.lasalle.medigest.dominio.citas.Cita;
import com.lasalle.medigest.dominio.citas.Observer;
import org.springframework.stereotype.Component;

@Component
public class NotificadorMedico implements Observer {

    @Override
    public void actualizar(Cita cita) {
        // Imprime una alerta en consola simulando la notificación al médico asignado
        System.out.println("\n[NOTIFICACIÓN MÉDICA] Alerta para el Dr(a). " + cita.getMedicoAsignado()
                + " -> La cita del paciente con ID " + (cita.getPaciente() != null ? cita.getPaciente().getId() : "N/A")
                + " ha cambiado al estado: " + cita.getEstado());
    }
}