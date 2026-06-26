package com.lasalle.medigest.dominio.excepcion;

/**
 * Excepción de dominio para indicar que un recurso solicitado
 * (paciente, historia clínica, cita, etc.) no existe.
 * El {@code ManejadorExcepciones} la traduce a HTTP 404.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
