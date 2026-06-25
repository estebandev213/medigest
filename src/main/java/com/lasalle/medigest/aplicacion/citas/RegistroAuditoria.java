package com.lasalle.medigest.aplicacion.citas;

import com.lasalle.medigest.dominio.citas.Cita;
import com.lasalle.medigest.dominio.citas.Observer;
import org.springframework.stereotype.Component;

@Component
public class RegistroAuditoria implements Observer {

    @Override
    public void actualizar(Cita cita) {
        // Imprime un log en consola simulando el registro histórico de auditoría
        System.out.println("[AUDITORÍA LOG] Cita ID: " + cita.getId() 
                + " ha sido modificada. Nuevo estado registrado: " + cita.getEstado());
    }
}