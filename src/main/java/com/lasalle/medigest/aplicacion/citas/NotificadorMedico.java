package com.lasalle.medigest.aplicacion.citas;

import com.lasalle.medigest.dominio.citas.Cita;
import com.lasalle.medigest.dominio.citas.Observer;
import org.springframework.stereotype.Component;

@Component
public class NotificadorMedico implements Observer {

    @Override
    public void actualizar(Cita cita) {
        System.out.printf("[NOTIFICADOR] Médico '%s' notificado — cita #%d cambió a %s%n",
                cita.getMedicoAsignado(), cita.getId(), cita.getEstado());
    }
}
