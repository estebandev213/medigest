package com.lasalle.medigest.aplicacion.admision;

import com.lasalle.medigest.dominio.admision.DatosPaciente;
import com.lasalle.medigest.dominio.admision.ValidadorIdentidad;
import com.lasalle.medigest.infraestructura.apiperu.ApiPeruClient;
import com.lasalle.medigest.infraestructura.apiperu.ApiPeruDniResponse;
import com.lasalle.medigest.infraestructura.apiperu.ApiPeruException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Patrón Adapter: adapta la interfaz de dominio ValidadorIdentidad
 * a la API externa de apiperu.dev (consulta DNI vía padrón reducido SUNAT).
 *
 * Se activa por defecto (cualquier perfil que NO sea "stub").
 * Si la API externa falla, lanza la excepción correspondiente.
 */
@Slf4j
@Component
@Profile("!stub")
@RequiredArgsConstructor
public class ReniecAdapter implements ValidadorIdentidad {

    private final ApiPeruClient apiPeruClient;

    @Override
    public DatosPaciente validar(String dni) {
        try {
            ApiPeruDniResponse response = apiPeruClient.consultarDni(dni);

            if (!response.isSuccess() || response.getData() == null) {
                throw new IllegalArgumentException("DNI no encontrado en RENIEC: " + dni);
            }

            ApiPeruDniResponse.DniData data = response.getData();

            // Concatenar apellido paterno + materno para el campo "apellidos" del dominio
            String apellidos = (data.getApellidoPaterno() + " " + data.getApellidoMaterno()).trim();

            return new DatosPaciente(
                    data.getNumero(),
                    data.getNombres(),
                    apellidos,
                    null,   // fechaNacimiento: no disponible en la API
                    null    // dirección: no disponible en la API
            );

        } catch (ApiPeruException e) {
            log.error("Error al consultar API Peru para DNI {}: {}", dni, e.getMessage());
            throw new IllegalArgumentException(
                    "No se pudo validar el DNI " + dni + ". Servicio externo no disponible.", e);
        }
    }
}
