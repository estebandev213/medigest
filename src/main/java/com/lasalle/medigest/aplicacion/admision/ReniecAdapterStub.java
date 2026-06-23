package com.lasalle.medigest.aplicacion.admision;

import com.lasalle.medigest.dominio.admision.DatosPaciente;
import com.lasalle.medigest.dominio.admision.ValidadorIdentidad;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Patrón Adapter (Stub): implementación con datos estáticos para pruebas y demos offline.
 * Se activa con el perfil Spring "stub": spring.profiles.active=stub
 */
@Component
@Profile("stub")
public class ReniecAdapterStub implements ValidadorIdentidad {

    private static final Map<String, DatosPaciente> STUB_RENIEC = Map.of(
        "12345678", new DatosPaciente("12345678", "Juan Carlos", "Pérez López",    "1990-05-15", "Av. Lima 123"),
        "87654321", new DatosPaciente("87654321", "María Elena", "García Torres",  "1985-11-22", "Jr. Cusco 456"),
        "11223344", new DatosPaciente("11223344", "Carlos",       "Quispe Mamani", "2000-03-08", "Calle Arequipa 789")
    );

    @Override
    public DatosPaciente validar(String dni) {
        DatosPaciente datos = STUB_RENIEC.get(dni);
        if (datos == null) {
            throw new IllegalArgumentException("DNI no encontrado en RENIEC (stub): " + dni);
        }
        return datos;
    }
}
