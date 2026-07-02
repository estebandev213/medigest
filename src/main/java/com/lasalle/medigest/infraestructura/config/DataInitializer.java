package com.lasalle.medigest.infraestructura.config;

import com.lasalle.medigest.dominio.admision.Paciente;
import com.lasalle.medigest.dominio.admision.TipoPaciente;
import com.lasalle.medigest.dominio.atencion.HistoriaClinica;
import com.lasalle.medigest.dominio.citas.Cita;
import com.lasalle.medigest.dominio.citas.EstadoCita;
import com.lasalle.medigest.dominio.facturacion.Factura;
import com.lasalle.medigest.dominio.facturacion.ServicioSimple;
import com.lasalle.medigest.dominio.facturacion.TipoCobertura;
import com.lasalle.medigest.persistencia.admision.PacienteRepository;
import com.lasalle.medigest.persistencia.atencion.HistoriaClinicaRepository;
import com.lasalle.medigest.persistencia.citas.CitaRepository;
import com.lasalle.medigest.persistencia.facturacion.FacturaRepository;
import com.lasalle.medigest.persistencia.facturacion.ServicioSimpleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final PacienteRepository pacienteRepo;
    private final CitaRepository citaRepo;
    private final HistoriaClinicaRepository historiaRepo;
    private final ServicioSimpleRepository servicioRepo;
    private final FacturaRepository facturaRepo;

    @Override
    public void run(ApplicationArguments args) {
        if (pacienteRepo.count() > 0) {
            log.info("Base de datos ya contiene datos — omitiendo inicialización.");
            return;
        }

        log.info("Inicializando base de datos con datos de demostración...");

        // ── Pacientes ────────────────────────────────────────────────────────

        Paciente p1 = pacienteRepo.save(Paciente.builder()
                .dni("42567891").nombres("Rosa María").apellidos("Huanca Condori")
                .fechaNacimiento("1988-03-14").telefono("987654321")
                .email("rosa.huanca@gmail.com").direccion("Av. Arequipa 2340, Lince")
                .tipo(TipoPaciente.SIS).build());

        Paciente p2 = pacienteRepo.save(Paciente.builder()
                .dni("38291045").nombres("Carlos Alberto").apellidos("Medina Torres")
                .fechaNacimiento("1975-11-28").telefono("998877665")
                .email("cmedina.torres@outlook.com").direccion("Jr. Camaná 560, Cercado de Lima")
                .tipo(TipoPaciente.PARTICULAR).build());

        Paciente p3 = pacienteRepo.save(Paciente.builder()
                .dni("47832156").nombres("Ana Lucía").apellidos("Quispe Flores")
                .fechaNacimiento("1995-07-05").telefono("976543218")
                .email("analucia.qf@gmail.com").direccion("Calle Los Álamos 120, Surco")
                .tipo(TipoPaciente.SEGURO_PRIVADO).build());

        Paciente p4 = pacienteRepo.save(Paciente.builder()
                .dni("52341678").nombres("José Luis").apellidos("Vargas Mamani")
                .fechaNacimiento("1982-09-19").telefono("945678123")
                .email("jlvargas@hotmail.com").direccion("Av. Tupac Amaru 1850, Comas")
                .tipo(TipoPaciente.SIS).build());

        Paciente p5 = pacienteRepo.save(Paciente.builder()
                .dni("45678923").nombres("Patricia Elena").apellidos("Soto Guzmán")
                .fechaNacimiento("1969-04-22").telefono("912345678")
                .email("psotoguzman@gmail.com").direccion("Malecón Grau 430, Miraflores")
                .tipo(TipoPaciente.PARTICULAR).build());

        Paciente p6 = pacienteRepo.save(Paciente.builder()
                .dni("39876543").nombres("Miguel Ángel").apellidos("Romero Cruz")
                .fechaNacimiento("2015-01-30").telefono("956781234")
                .email("familia.romero@gmail.com").direccion("Av. Brasil 890, Breña")
                .tipo(TipoPaciente.SEGURO_PRIVADO).build());

        Paciente p7 = pacienteRepo.save(Paciente.builder()
                .dni("56234189").nombres("Carmen Rosa").apellidos("Quispe Tapia")
                .fechaNacimiento("1991-06-17").telefono("934512678")
                .email("carmen.qt@gmail.com").direccion("Jr. Piura 340, La Victoria")
                .tipo(TipoPaciente.SIS).build());

        Paciente p8 = pacienteRepo.save(Paciente.builder()
                .dni("48123956").nombres("Eduardo Javier").apellidos("Campos López")
                .fechaNacimiento("1980-12-03").telefono("967890123")
                .email("ecampos.lopez@empresa.pe").direccion("Av. Javier Prado Este 1200, San Isidro")
                .tipo(TipoPaciente.PARTICULAR).build());

        Paciente p9 = pacienteRepo.save(Paciente.builder()
                .dni("63451287").nombres("Sofía Isabel").apellidos("Mendoza Ramos")
                .fechaNacimiento("1998-08-25").telefono("923456789")
                .email("sofia.mendoza@gmail.com").direccion("Calle Alcanfores 210, Miraflores")
                .tipo(TipoPaciente.SEGURO_PRIVADO).build());

        Paciente p10 = pacienteRepo.save(Paciente.builder()
                .dni("44567832").nombres("Roberto Carlos").apellidos("Pinto Navarro")
                .fechaNacimiento("1962-02-14").telefono("989012345")
                .email("rpinto.nav@gmail.com").direccion("Av. Universitaria 2540, San Miguel")
                .tipo(TipoPaciente.PARTICULAR).build());

        Paciente p11 = pacienteRepo.save(Paciente.builder()
                .dni("51234678").nombres("Lucía Fernanda").apellidos("Herrera Vega")
                .fechaNacimiento("2018-11-10").telefono("978901234")
                .email("familia.herrera@gmail.com").direccion("Jr. Cusco 780, Cercado de Lima")
                .tipo(TipoPaciente.SIS).build());

        Paciente p12 = pacienteRepo.save(Paciente.builder()
                .dni("37891245").nombres("Diego Alejandro").apellidos("Castro Ríos")
                .fechaNacimiento("1987-05-08").telefono("965432109")
                .email("dcastro.rios@gmail.com").direccion("Av. Salaverry 3100, Jesús María")
                .tipo(TipoPaciente.SEGURO_PRIVADO).build());

        // ── Servicios facturables ────────────────────────────────────────────

        ServicioSimple sConsultaGeneral   = servicioRepo.save(new ServicioSimple("Consulta Médica General",      80.00));
        ServicioSimple sConsultaCardio    = servicioRepo.save(new ServicioSimple("Consulta de Cardiología",      150.00));
        ServicioSimple sConsultaPediatria = servicioRepo.save(new ServicioSimple("Consulta Pediátrica",          90.00));
        ServicioSimple sConsultaGineco    = servicioRepo.save(new ServicioSimple("Consulta Ginecológica",        120.00));
        ServicioSimple sHemograma         = servicioRepo.save(new ServicioSimple("Hemograma Completo",           45.00));
        ServicioSimple sECG               = servicioRepo.save(new ServicioSimple("Electrocardiograma (ECG)",     65.00));
        ServicioSimple sEcografia         = servicioRepo.save(new ServicioSimple("Ecografía Abdominal",         130.00));
        ServicioSimple sRxTorax           = servicioRepo.save(new ServicioSimple("Radiografía de Tórax",         80.00));
        ServicioSimple sGlucosa           = servicioRepo.save(new ServicioSimple("Prueba de Glucosa en Ayunas",  25.00));
        ServicioSimple sControl           = servicioRepo.save(new ServicioSimple("Control de Presión Arterial",  30.00));

        // ── Citas (ATENDIDAS — base para historias) ──────────────────────────

        Cita c1 = citaRepo.save(Cita.builder()
                .paciente(p2).medicoAsignado("Dr. Esteban Villagarcia").especialidad("Cardiología")
                .fechaHora(LocalDateTime.now().minusDays(30)).estado(EstadoCita.ATENDIDA)
                .motivo("Dolor en el pecho y palpitaciones irregulares").build());

        Cita c2 = citaRepo.save(Cita.builder()
                .paciente(p5).medicoAsignado("Dr. Keiko Carpio").especialidad("Cardiología")
                .fechaHora(LocalDateTime.now().minusDays(22)).estado(EstadoCita.ATENDIDA)
                .motivo("Control post-infarto, evaluación de función cardíaca").build());

        Cita c3 = citaRepo.save(Cita.builder()
                .paciente(p6).medicoAsignado("Dr. Leonidas Frames").especialidad("Pediatría")
                .fechaHora(LocalDateTime.now().minusDays(18)).estado(EstadoCita.ATENDIDA)
                .motivo("Fiebre persistente y tos seca por más de 5 días").build());

        Cita c4 = citaRepo.save(Cita.builder()
                .paciente(p1).medicoAsignado("Dr. Jeanfranco Lazarinos").especialidad("Medicina General")
                .fechaHora(LocalDateTime.now().minusDays(14)).estado(EstadoCita.ATENDIDA)
                .motivo("Control rutinario y renovación de medicación crónica").build());

        Cita c5 = citaRepo.save(Cita.builder()
                .paciente(p3).medicoAsignado("Dr. Franco Newvillage").especialidad("Ginecología")
                .fechaHora(LocalDateTime.now().minusDays(10)).estado(EstadoCita.ATENDIDA)
                .motivo("Revisión ginecológica anual y Papanicolaou").build());

        Cita c6 = citaRepo.save(Cita.builder()
                .paciente(p10).medicoAsignado("Dr. Esteban Villagarcia").especialidad("Cardiología")
                .fechaHora(LocalDateTime.now().minusDays(8)).estado(EstadoCita.ATENDIDA)
                .motivo("Evaluación de hipertensión arterial y ajuste de tratamiento").build());

        Cita c7 = citaRepo.save(Cita.builder()
                .paciente(p4).medicoAsignado("Dr. Fernando Lizarraga").especialidad("Medicina General")
                .fechaHora(LocalDateTime.now().minusDays(5)).estado(EstadoCita.ATENDIDA)
                .motivo("Malestar gastrointestinal y evaluación de gastritis crónica").build());

        Cita c8 = citaRepo.save(Cita.builder()
                .paciente(p11).medicoAsignado("Dr. Leonidas Frames").especialidad("Pediatría")
                .fechaHora(LocalDateTime.now().minusDays(3)).estado(EstadoCita.ATENDIDA)
                .motivo("Control de crecimiento y desarrollo, vacunación al día").build());

        // ── Citas (CONFIRMADAS — próximas) ───────────────────────────────────

        Cita c9 = citaRepo.save(Cita.builder()
                .paciente(p7).medicoAsignado("Dr. Jeanfranco Lazarinos").especialidad("Medicina General")
                .fechaHora(LocalDateTime.now().plusDays(1).withHour(9).withMinute(0))
                .estado(EstadoCita.CONFIRMADA)
                .motivo("Evaluación de diabetes tipo 2 y control glucémico").build());

        Cita c10 = citaRepo.save(Cita.builder()
                .paciente(p9).medicoAsignado("Dr. Franco Newvillage").especialidad("Ginecología")
                .fechaHora(LocalDateTime.now().plusDays(2).withHour(10).withMinute(30))
                .estado(EstadoCita.CONFIRMADA)
                .motivo("Consulta por irregularidades menstruales y ecografía pélvica").build());

        Cita c11 = citaRepo.save(Cita.builder()
                .paciente(p8).medicoAsignado("Dr. Fernando Lizarraga").especialidad("Medicina General")
                .fechaHora(LocalDateTime.now().plusDays(3).withHour(11).withMinute(0))
                .estado(EstadoCita.CONFIRMADA)
                .motivo("Chequeo ejecutivo anual — evaluación integral de salud").build());

        Cita c12 = citaRepo.save(Cita.builder()
                .paciente(p12).medicoAsignado("Dr. Keiko Carpio").especialidad("Cardiología")
                .fechaHora(LocalDateTime.now().plusDays(4).withHour(14).withMinute(0))
                .estado(EstadoCita.CONFIRMADA)
                .motivo("Evaluación de arritmia supraventricular, Holter 24h").build());

        // ── Citas (PENDIENTES — por confirmar) ───────────────────────────────

        citaRepo.save(Cita.builder()
                .paciente(p2).medicoAsignado("Dr. Esteban Villagarcia").especialidad("Cardiología")
                .fechaHora(LocalDateTime.now().plusDays(7).withHour(8).withMinute(30))
                .estado(EstadoCita.PENDIENTE)
                .motivo("Seguimiento post-tratamiento de insuficiencia cardíaca").build());

        citaRepo.save(Cita.builder()
                .paciente(p1).medicoAsignado("Dr. Jeanfranco Lazarinos").especialidad("Medicina General")
                .fechaHora(LocalDateTime.now().plusDays(9).withHour(15).withMinute(0))
                .estado(EstadoCita.PENDIENTE)
                .motivo("Resultado de análisis de laboratorio y ajuste de medicación").build());

        citaRepo.save(Cita.builder()
                .paciente(p6).medicoAsignado("Dr. Leonidas Frames").especialidad("Pediatría")
                .fechaHora(LocalDateTime.now().plusDays(12).withHour(9).withMinute(0))
                .estado(EstadoCita.PENDIENTE)
                .motivo("Control mensual de asma bronquial infantil").build());

        // ── Citas (CANCELADAS) ────────────────────────────────────────────────

        citaRepo.save(Cita.builder()
                .paciente(p4).medicoAsignado("Dr. Fernando Lizarraga").especialidad("Medicina General")
                .fechaHora(LocalDateTime.now().minusDays(2).withHour(16).withMinute(0))
                .estado(EstadoCita.CANCELADA)
                .motivo("Control de anemia — paciente no se presentó").build());

        // ── Historias clínicas ────────────────────────────────────────────────

        historiaRepo.save(HistoriaClinica.builder()
                .paciente(p2).cita(c1).fechaAtencion(LocalDate.now().minusDays(30))
                .medicoTratante("Dr. Esteban Villagarcia")
                .diagnostico("Cardiopatía isquémica estable. Episodio de angina de pecho. "
                        + "Se observa en ECG alteraciones del segmento ST en derivaciones V3-V5. "
                        + "Presión arterial: 145/90 mmHg.")
                .alergias("Aspirina (urticaria), Penicilina")
                .resultadosLaboratorio("Troponina I: 0.02 ng/mL (normal). Colesterol total: 238 mg/dL (elevado). "
                        + "LDL: 158 mg/dL. HDL: 42 mg/dL. Triglicéridos: 192 mg/dL.")
                .tratamiento("Atorvastatina 40mg/día. Metoprolol 50mg/12h. "
                        + "Nitroglicerina sublingual SOS. Dieta hiposódica e hipograsa. "
                        + "Restricción de actividad física intensa. Control en 30 días.").build());

        historiaRepo.save(HistoriaClinica.builder()
                .paciente(p5).cita(c2).fechaAtencion(LocalDate.now().minusDays(22))
                .medicoTratante("Dr. Keiko Carpio")
                .diagnostico("Seguimiento post-infarto agudo de miocardio (6 meses). "
                        + "Fracción de eyección ventricular izquierda: 52% (levemente reducida). "
                        + "Sin nuevos episodios isquémicos desde el evento.")
                .alergias("Sin alergias medicamentosas conocidas")
                .resultadosLaboratorio("BNP: 85 pg/mL (límite superior). Creatinina: 0.9 mg/dL (normal). "
                        + "Hemograma: dentro de rangos normales. Perfil lipídico controlado.")
                .tratamiento("Enalapril 10mg/día. Carvedilol 12.5mg/12h. AAS 100mg/día. "
                        + "Eplerenona 25mg/día. Programa de rehabilitación cardíaca fase II. "
                        + "Ecocardiograma de control en 3 meses.").build());

        historiaRepo.save(HistoriaClinica.builder()
                .paciente(p6).cita(c3).fechaAtencion(LocalDate.now().minusDays(18))
                .medicoTratante("Dr. Leonidas Frames")
                .diagnostico("Bronquitis aguda viral con componente alérgico. "
                        + "Temperatura axilar: 38.4°C. Saturación O2: 97%. "
                        + "Auscultación: sibilancias bilaterales difusas.")
                .alergias("Polen (rinitis alérgica estacional), polvo doméstico")
                .resultadosLaboratorio("Hemograma: leucocitos 9800 /μL (normal), linfocitos 42% (elevado). "
                        + "PCR: 12 mg/L (leve elevación). Rx tórax: sin consolidaciones.")
                .tratamiento("Salbutamol MDI 2 puffs c/6h x 5 días. Prednisolona 1mg/kg/día x 3 días. "
                        + "Ambroxol jarabe 15mg c/8h. Abundantes líquidos. "
                        + "Control en 7 días o antes si empeora. Evitar exposición a alérgenos.").build());

        historiaRepo.save(HistoriaClinica.builder()
                .paciente(p1).cita(c4).fechaAtencion(LocalDate.now().minusDays(14))
                .medicoTratante("Dr. Jeanfranco Lazarinos")
                .diagnostico("Hipotiroidismo primario en tratamiento. Control rutinario. "
                        + "Paciente refiere mejoría de síntomas (fatiga, frío). "
                        + "Peso estable. PA: 118/76 mmHg. FC: 72 lpm.")
                .alergias("Sin alergias conocidas")
                .resultadosLaboratorio("TSH: 2.8 mUI/L (normal con tratamiento). T4 libre: 1.2 ng/dL (normal). "
                        + "Hemograma completo: dentro de rangos normales. Perfil bioquímico normal.")
                .tratamiento("Levotiroxina 75mcg en ayunas, mantener dosis actual. "
                        + "Control de TSH en 6 meses. Dieta balanceada. "
                        + "Se indica continuar con ejercicio moderado diario.").build());

        historiaRepo.save(HistoriaClinica.builder()
                .paciente(p3).cita(c5).fechaAtencion(LocalDate.now().minusDays(10))
                .medicoTratante("Dr. Franco Newvillage")
                .diagnostico("Revisión ginecológica anual sin hallazgos patológicos. "
                        + "Ciclos menstruales regulares. Exploración pélvica normal. "
                        + "Mamas sin nódulos palpables.")
                .alergias("Látex (irritación cutánea leve)")
                .resultadosLaboratorio("Papanicolaou: NILM (negativo para lesión intraepitelial o malignidad). "
                        + "Test VPH: negativo. Ecografía transvaginal: útero y anejos normales.")
                .tratamiento("Sin tratamiento indicado. Continuar anticonceptivos orales actuales. "
                        + "Mamografía preventiva en 2 años (por historia familiar). "
                        + "Control ginecológico anual. Auto-exploración mamaria mensual.").build());

        historiaRepo.save(HistoriaClinica.builder()
                .paciente(p10).cita(c6).fechaAtencion(LocalDate.now().minusDays(8))
                .medicoTratante("Dr. Esteban Villagarcia")
                .diagnostico("Hipertensión arterial esencial grado II, no controlada. "
                        + "PA en consulta: 168/102 mmHg (promedio 3 tomas). "
                        + "Refiere cefalea occipital matutina frecuente.")
                .alergias("IECA (tos seca intolerable — confirmado con enalapril)")
                .resultadosLaboratorio("Creatinina: 1.1 mg/dL. Potasio: 4.2 mEq/L. "
                        + "Orina: microalbuminuria 38 mg/24h (elevada). ECG: hipertrofia ventricular izquierda leve.")
                .tratamiento("Losartán 100mg/día (cambio de IECA a ARA-II por intolerancia). "
                        + "Amlodipino 10mg/día. Restricción sódica estricta (<2g/día). "
                        + "MAPA 24h. Control en 2 semanas. Derivar a Nefrología si persiste microalbuminuria.").build());

        historiaRepo.save(HistoriaClinica.builder()
                .paciente(p4).cita(c7).fechaAtencion(LocalDate.now().minusDays(5))
                .medicoTratante("Dr. Fernando Lizarraga")
                .diagnostico("Gastritis crónica antral leve con probable infección por H. pylori. "
                        + "Epigastralgia postprandial de 3 semanas de evolución. "
                        + "Abdomen: dolor a la palpación en epigastrio.")
                .alergias("AINEs (gastrolesividad comprobada)")
                .resultadosLaboratorio("Antígeno fecal H. pylori: POSITIVO. Hemograma: sin anemia. "
                        + "Amilasa y lipasa: normales. Endoscopía pendiente.")
                .tratamiento("Esquema erradicador H. pylori: Omeprazol 20mg + Amoxicilina 1g + Claritromicina 500mg, c/12h x 14 días. "
                        + "Evitar AINEs, alcohol y café. Dieta blanda fraccionada. "
                        + "Endoscopía digestiva alta en 4 semanas. Control clínico en 3 semanas.").build());

        historiaRepo.save(HistoriaClinica.builder()
                .paciente(p11).cita(c8).fechaAtencion(LocalDate.now().minusDays(3))
                .medicoTratante("Dr. Leonidas Frames")
                .diagnostico("Niña de 7 años con desarrollo psicomotor adecuado para la edad. "
                        + "Peso: 22 kg (P50). Talla: 119 cm (P50). IMC: 15.5 (normal). "
                        + "Vacunación completa según esquema nacional.")
                .alergias("Sin alergias conocidas")
                .resultadosLaboratorio("Hemoglobina: 12.1 g/dL (normal para edad). Ferritina: 28 ng/mL. "
                        + "Parásitos en heces: negativo. Vitamina D: 28 ng/mL (insuficiente leve).")
                .tratamiento("Vitamina D 1000 UI/día por 3 meses. "
                        + "Dieta variada con énfasis en hierro no hemínico (legumbres, verduras). "
                        + "Actividad física mínima 60 min/día. "
                        + "Próximo control en 6 meses o por enfermedad aguda.").build());

        // ── Facturas ──────────────────────────────────────────────────────────

        facturaRepo.save(Factura.builder()
                .paciente(p2).item(sConsultaCardio).fechaEmision(LocalDateTime.now().minusDays(30))
                .tipoCobertura(TipoCobertura.PARTICULAR).costoTotal(150.00)
                .montoCubierto(0.00).montoPaciente(150.00)
                .detalle("Consulta cardiológica — Dr. Esteban Villagarcia").build());

        facturaRepo.save(Factura.builder()
                .paciente(p2).item(sECG).fechaEmision(LocalDateTime.now().minusDays(30))
                .tipoCobertura(TipoCobertura.PARTICULAR).costoTotal(65.00)
                .montoCubierto(0.00).montoPaciente(65.00)
                .detalle("Electrocardiograma de 12 derivaciones").build());

        facturaRepo.save(Factura.builder()
                .paciente(p5).item(sConsultaCardio).fechaEmision(LocalDateTime.now().minusDays(22))
                .tipoCobertura(TipoCobertura.SEGURO_PRIVADO).costoTotal(150.00)
                .montoCubierto(120.00).montoPaciente(30.00)
                .detalle("Consulta post-IAM — Dr. Keiko Carpio — Cobertura Rímac 80%").build());

        facturaRepo.save(Factura.builder()
                .paciente(p6).item(sConsultaPediatria).fechaEmision(LocalDateTime.now().minusDays(18))
                .tipoCobertura(TipoCobertura.SEGURO_PRIVADO).costoTotal(90.00)
                .montoCubierto(72.00).montoPaciente(18.00)
                .detalle("Consulta pediátrica — Dr. Leonidas Frames — Cobertura Pacífico 80%").build());

        facturaRepo.save(Factura.builder()
                .paciente(p1).item(sConsultaGeneral).fechaEmision(LocalDateTime.now().minusDays(14))
                .tipoCobertura(TipoCobertura.SIS).costoTotal(80.00)
                .montoCubierto(80.00).montoPaciente(0.00)
                .detalle("Consulta medicina general — Dr. Jeanfranco Lazarinos — SIS 100%").build());

        facturaRepo.save(Factura.builder()
                .paciente(p1).item(sHemograma).fechaEmision(LocalDateTime.now().minusDays(14))
                .tipoCobertura(TipoCobertura.SIS).costoTotal(45.00)
                .montoCubierto(45.00).montoPaciente(0.00)
                .detalle("Hemograma completo — cobertura SIS").build());

        facturaRepo.save(Factura.builder()
                .paciente(p3).item(sConsultaGineco).fechaEmision(LocalDateTime.now().minusDays(10))
                .tipoCobertura(TipoCobertura.SEGURO_PRIVADO).costoTotal(120.00)
                .montoCubierto(96.00).montoPaciente(24.00)
                .detalle("Revisión ginecológica anual — Dr. Franco Newvillage — Cobertura La Positiva 80%").build());

        facturaRepo.save(Factura.builder()
                .paciente(p10).item(sConsultaCardio).fechaEmision(LocalDateTime.now().minusDays(8))
                .tipoCobertura(TipoCobertura.PARTICULAR).costoTotal(150.00)
                .montoCubierto(0.00).montoPaciente(150.00)
                .detalle("Consulta cardiológica por HTA — Dr. Esteban Villagarcia").build());

        facturaRepo.save(Factura.builder()
                .paciente(p10).item(sControl).fechaEmision(LocalDateTime.now().minusDays(8))
                .tipoCobertura(TipoCobertura.PARTICULAR).costoTotal(30.00)
                .montoCubierto(0.00).montoPaciente(30.00)
                .detalle("Control de presión arterial — monitoreo 3 tomas").build());

        facturaRepo.save(Factura.builder()
                .paciente(p4).item(sConsultaGeneral).fechaEmision(LocalDateTime.now().minusDays(5))
                .tipoCobertura(TipoCobertura.SIS).costoTotal(80.00)
                .montoCubierto(80.00).montoPaciente(0.00)
                .detalle("Consulta medicina general — Dr. Fernando Lizarraga — SIS 100%").build());

        facturaRepo.save(Factura.builder()
                .paciente(p11).item(sConsultaPediatria).fechaEmision(LocalDateTime.now().minusDays(3))
                .tipoCobertura(TipoCobertura.SIS).costoTotal(90.00)
                .montoCubierto(90.00).montoPaciente(0.00)
                .detalle("Control pediátrico — Dr. Leonidas Frames — SIS 100%").build());

        facturaRepo.save(Factura.builder()
                .paciente(p12).item(sEcografia).fechaEmision(LocalDateTime.now().minusDays(1))
                .tipoCobertura(TipoCobertura.SEGURO_PRIVADO).costoTotal(130.00)
                .montoCubierto(104.00).montoPaciente(26.00)
                .detalle("Ecografía abdominal preventiva — Cobertura Mapfre 80%").build());

        log.info("Inicialización completada: 12 pacientes, 16 citas, 8 historias clínicas, 10 servicios, 12 facturas.");
    }
}
