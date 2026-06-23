package com.lasalle.medigest.infraestructura.apiperu;

/**
 * Excepción personalizada para errores de comunicación con la API de apiperu.dev.
 */
public class ApiPeruException extends RuntimeException {

    public ApiPeruException(String message) {
        super(message);
    }

    public ApiPeruException(String message, Throwable cause) {
        super(message, cause);
    }
}
