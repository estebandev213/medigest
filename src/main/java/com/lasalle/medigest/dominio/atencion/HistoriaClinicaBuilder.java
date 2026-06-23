package com.lasalle.medigest.dominio.atencion;

public interface HistoriaClinicaBuilder {
    HistoriaClinicaBuilder conDiagnostico(String diagnostico);
    HistoriaClinicaBuilder conAlergias(String alergias);
    HistoriaClinicaBuilder conResultadosLaboratorio(String resultados);
    HistoriaClinicaBuilder conTratamiento(String tratamiento);
    HistoriaClinica construir();
}
