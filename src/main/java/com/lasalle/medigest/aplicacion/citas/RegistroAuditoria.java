package com.lasalle.medigest.aplicacion.citas;

import com.lasalle.medigest.dominio.citas.Cita;
import com.lasalle.medigest.dominio.citas.Observer;
import org.springframework.stereotype.Component;

@Component
public class RegistroAuditoria implements Observer {

    @Override
    public void actualizar(Cita cita) {
        String pacienteDni = cita.getPaciente() != null ? cita.getPaciente().getDni() : "N/A";
        System.out.printf("[AUDITORIA] Cita #%d | Paciente DNI: %s | Médico: %s | Estado: %s%n",
                cita.getId(), pacienteDni, cita.getMedicoAsignado(), cita.getEstado());
    }
}
