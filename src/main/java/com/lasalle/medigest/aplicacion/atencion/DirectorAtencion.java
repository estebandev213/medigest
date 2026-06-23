package com.lasalle.medigest.aplicacion.atencion;

import com.lasalle.medigest.dominio.atencion.HistoriaClinica;
import com.lasalle.medigest.dominio.atencion.HistoriaClinicaBuilder;

public class DirectorAtencion {

    private final HistoriaClinicaBuilder builder;

    public DirectorAtencion(HistoriaClinicaBuilder builder) {
        this.builder = builder;
    }

    public HistoriaClinica construirHistoriaCompleta(
            String diagnostico, String alergias, String resultados, String tratamiento) {
        return builder
                .conDiagnostico(diagnostico)
                .conAlergias(alergias)
                .conResultadosLaboratorio(resultados)
                .conTratamiento(tratamiento)
                .construir();
    }

    public HistoriaClinica construirHistoriaBasica(String diagnostico, String tratamiento) {
        return builder
                .conDiagnostico(diagnostico)
                .conTratamiento(tratamiento)
                .construir();
    }
}
