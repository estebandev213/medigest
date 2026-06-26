package com.lasalle.medigest.presentacion;

import com.lasalle.medigest.aplicacion.admision.ServicioAdmision;
import com.lasalle.medigest.aplicacion.atencion.ServicioAtencion;
import com.lasalle.medigest.aplicacion.citas.ServicioCitas;
import com.lasalle.medigest.aplicacion.facturacion.ServicioFacturacion;
import com.lasalle.medigest.dominio.admision.Paciente;
import com.lasalle.medigest.dominio.admision.TipoPaciente;
import com.lasalle.medigest.dominio.atencion.HistoriaClinica;
import com.lasalle.medigest.dominio.citas.Cita;
import com.lasalle.medigest.dominio.citas.EstadoCita;
import com.lasalle.medigest.dominio.excepcion.RecursoNoEncontradoException;
import com.lasalle.medigest.dominio.facturacion.TipoCobertura;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class WebController {

    private static final String SESSION_USUARIO = "usuarioLogueado";

    private final ServicioCitas servicioCitas;
    private final ServicioAdmision servicioAdmision;
    private final ServicioAtencion servicioAtencion;
    private final ServicioFacturacion servicioFacturacion;

    // ── Público ─────────────────────────────────────────────────────────────

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        if ("admin".equals(username) && "admin".equals(password)) {
            session.setAttribute(SESSION_USUARIO, username);
            return "redirect:/dashboard";
        }

        model.addAttribute("error", "Usuario o contraseña incorrectos.");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // ── Dashboard (requiere sesión) ─────────────────────────────────────────

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String redirect = requiereSesion(session);
        if (redirect != null) {
            return redirect;
        }

        List<Cita> citas = servicioCitas.listarTodas();
        model.addAttribute("misCitas", citas);
        model.addAttribute("totalCitas", citas.size());
        model.addAttribute("listaPacientes", servicioAdmision.listarTodos());
        return "dashboard";
    }

    @PostMapping("/dashboard/programar-cita")
    public String programarCita(
            HttpSession session,
            @RequestParam Long pacienteId,
            @RequestParam String medicoAsignado,
            @RequestParam String especialidad,
            @RequestParam String fechaHora,
            @RequestParam String motivo,
            RedirectAttributes redirectAttributes) {

        String redirect = requiereSesion(session);
        if (redirect != null) {
            return redirect;
        }

        Paciente paciente = servicioAdmision.buscarPorId(pacienteId)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado: " + pacienteId));

        Cita cita = Cita.builder()
                .paciente(paciente)
                .medicoAsignado(medicoAsignado)
                .especialidad(especialidad)
                .fechaHora(LocalDateTime.parse(fechaHora))
                .motivo(motivo)
                .build();

        servicioCitas.programarCita(cita);
        flashExito(redirectAttributes, "Cita programada correctamente.");
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/citas/{id}/confirmar")
    public String confirmarCita(HttpSession session, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        String redirect = requiereSesion(session);
        if (redirect != null) {
            return redirect;
        }
        servicioCitas.confirmarCita(id);
        flashExito(redirectAttributes, "Cita confirmada.");
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/citas/{id}/cancelar")
    public String cancelarCita(HttpSession session, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        String redirect = requiereSesion(session);
        if (redirect != null) {
            return redirect;
        }
        servicioCitas.cancelarCita(id);
        flashExito(redirectAttributes, "Cita cancelada.");
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/citas/{id}/atender")
    public String marcarAtendida(HttpSession session, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        String redirect = requiereSesion(session);
        if (redirect != null) {
            return redirect;
        }
        servicioCitas.cambiarEstadoGenerico(id, EstadoCita.ATENDIDA);
        flashExito(redirectAttributes, "Cita marcada como atendida.");
        return "redirect:/dashboard";
    }

    // ── Pacientes ───────────────────────────────────────────────────────────

    @GetMapping("/dashboard/pacientes")
    public String listarPacientes(
            HttpSession session,
            Model model,
            @RequestParam(required = false) String dni) {

        String redirect = requiereSesion(session);
        if (redirect != null) {
            return redirect;
        }

        List<Paciente> pacientes = (dni != null && !dni.isBlank())
                ? servicioAdmision.buscarPorDni(dni.trim()).map(List::of).orElse(List.of())
                : servicioAdmision.listarTodos();

        model.addAttribute("listaPacientes", pacientes);
        model.addAttribute("totalPacientes", pacientes.size());
        model.addAttribute("dniFiltro", dni);
        return "pacientes";
    }

    @PostMapping("/dashboard/pacientes/registrar")
    public String registrarPaciente(
            HttpSession session,
            @RequestParam String dni,
            @RequestParam String nombres,
            @RequestParam String apellidos,
            @RequestParam(required = false, defaultValue = "") String telefono,
            @RequestParam(required = false, defaultValue = "") String direccion,
            @RequestParam(required = false, defaultValue = "") String email,
            @RequestParam(required = false, defaultValue = "") String fechaNacimiento,
            @RequestParam String tipo,
            RedirectAttributes redirectAttributes) {

        String redirect = requiereSesion(session);
        if (redirect != null) {
            return redirect;
        }

        Paciente paciente = Paciente.builder()
                .dni(dni)
                .nombres(nombres)
                .apellidos(apellidos)
                .telefono(telefono)
                .direccion(direccion)
                .email(email)
                .fechaNacimiento(fechaNacimiento)
                .tipo(TipoPaciente.valueOf(tipo.toUpperCase()))
                .build();

        try {
            servicioAdmision.registrarPaciente(paciente);
            flashExito(redirectAttributes, "Paciente registrado correctamente.");
        } catch (IllegalStateException e) {
            flashError(redirectAttributes, e.getMessage());
        }
        return "redirect:/dashboard/pacientes";
    }

    // ── Historial clínico ───────────────────────────────────────────────────

    @GetMapping("/dashboard/historial-clinico")
    public String historialClinico(
            HttpSession session,
            Model model,
            @RequestParam(required = false) Long pacienteId) {

        String redirect = requiereSesion(session);
        if (redirect != null) {
            return redirect;
        }

        List<Paciente> pacientes = servicioAdmision.listarTodos();
        List<HistoriaClinica> historias = pacienteId != null
                ? servicioAtencion.listarPorPaciente(pacienteId)
                : servicioAtencion.listarTodas();

        Set<Long> citasOcupadas = servicioAtencion.listarCitaIdsConHistoria();
        List<Cita> citasDisponibles = servicioCitas.listarTodas().stream()
                .filter(c -> !citasOcupadas.contains(c.getId()))
                .toList();

        model.addAttribute("historias", historias);
        model.addAttribute("totalHistorias", historias.size());
        model.addAttribute("listaPacientes", pacientes);
        model.addAttribute("citasDisponibles", citasDisponibles);
        model.addAttribute("pacienteFiltro", pacienteId);
        if (pacienteId != null) {
            pacientes.stream()
                    .filter(p -> p.getId().equals(pacienteId))
                    .findFirst()
                    .ifPresent(p -> model.addAttribute("pacienteFiltroNombre",
                            p.getNombres() + " " + p.getApellidos()));
        }
        return "historial";
    }

    @GetMapping("/dashboard/historial-clinico/{id}")
    public String detalleHistoria(
            HttpSession session,
            @PathVariable Long id,
            Model model) {

        String redirect = requiereSesion(session);
        if (redirect != null) {
            return redirect;
        }

        return servicioAtencion.buscarPorId(id)
                .map(historia -> {
                    model.addAttribute("historia", historia);
                    return "historial-detalle";
                })
                .orElse("redirect:/dashboard/historial-clinico");
    }

    @PostMapping("/dashboard/historial-clinico/crear")
    public String crearHistoriaClinica(
            HttpSession session,
            @RequestParam Long pacienteId,
            @RequestParam(required = false, defaultValue = "") String citaId,
            @RequestParam String medicoTratante,
            @RequestParam String diagnostico,
            @RequestParam(required = false, defaultValue = "") String alergias,
            @RequestParam(required = false, defaultValue = "") String resultadosLaboratorio,
            @RequestParam(required = false, defaultValue = "") String tratamiento,
            RedirectAttributes redirectAttributes) {

        String redirect = requiereSesion(session);
        if (redirect != null) {
            return redirect;
        }

        Long citaIdLong = citaId.isBlank() ? null : Long.valueOf(citaId);
        String redirectHistorial = "redirect:/dashboard/historial-clinico?pacienteId=" + pacienteId;

        try {
            servicioAtencion.crearHistoriaClinica(
                    pacienteId, citaIdLong, medicoTratante, diagnostico,
                    alergias, resultadosLaboratorio, tratamiento);
            flashExito(redirectAttributes, "Historia clínica creada correctamente.");
            return redirectHistorial;
        } catch (IllegalStateException | IllegalArgumentException | RecursoNoEncontradoException e) {
            flashError(redirectAttributes, e.getMessage());
            return redirectHistorial;
        } catch (DataIntegrityViolationException e) {
            flashError(redirectAttributes,
                    "No se pudo vincular la cita: ya existe una historia clínica asociada. "
                            + "Elija otra cita o déjela sin vincular.");
            return redirectHistorial;
        }
    }

    @PostMapping("/dashboard/historial-clinico/{id}/laboratorio")
    public String agregarResultadosLaboratorio(
            HttpSession session,
            @PathVariable Long id,
            @RequestParam String resultados,
            RedirectAttributes redirectAttributes) {

        String redirect = requiereSesion(session);
        if (redirect != null) {
            return redirect;
        }

        servicioAtencion.agregarResultadosLaboratorio(id, resultados);
        flashExito(redirectAttributes, "Resultados de laboratorio guardados.");
        return "redirect:/dashboard/historial-clinico/" + id;
    }

    // ── Facturación ─────────────────────────────────────────────────────────

    @GetMapping("/dashboard/facturacion")
    public String facturacion(HttpSession session, Model model) {
        String redirect = requiereSesion(session);
        if (redirect != null) {
            return redirect;
        }

        var facturas = servicioFacturacion.listarFacturas();
        model.addAttribute("facturas", facturas);
        model.addAttribute("totalFacturas", facturas.size());
        model.addAttribute("servicios", servicioFacturacion.listarServicios());
        model.addAttribute("listaPacientes", servicioAdmision.listarTodos());
        return "facturacion";
    }

    @PostMapping("/dashboard/facturacion/generar")
    public String generarFactura(
            HttpSession session,
            @RequestParam Long pacienteId,
            @RequestParam Long itemId,
            @RequestParam String tipoCobertura,
            RedirectAttributes redirectAttributes) {

        String redirect = requiereSesion(session);
        if (redirect != null) {
            return redirect;
        }

        servicioFacturacion.generarFactura(
                pacienteId, itemId, TipoCobertura.valueOf(tipoCobertura.toUpperCase()));
        flashExito(redirectAttributes, "Factura generada correctamente.");
        return "redirect:/dashboard/facturacion";
    }

    @PostMapping("/dashboard/facturacion/servicios")
    public String crearServicio(
            HttpSession session,
            @RequestParam String nombre,
            @RequestParam double precioBase,
            RedirectAttributes redirectAttributes) {

        String redirect = requiereSesion(session);
        if (redirect != null) {
            return redirect;
        }

        servicioFacturacion.crearServicioSimple(nombre, precioBase);
        flashExito(redirectAttributes, "Servicio \"" + nombre + "\" creado.");
        return "redirect:/dashboard/facturacion";
    }

    @GetMapping("/dashboard/perfil")
    public String perfil(HttpSession session, Model model) {
        String redirect = requiereSesion(session);
        if (redirect != null) {
            return redirect;
        }
        model.addAttribute("usuario", session.getAttribute(SESSION_USUARIO));
        return "perfil";
    }

    @GetMapping("/dashboard/buscar-medicos")
    public String buscarMedicos(HttpSession session, Model model) {
        String redirect = requiereSesion(session);
        if (redirect != null) {
            return redirect;
        }

        List<MedicoDto> medicos = List.of(
                new MedicoDto("Dr. Esteban Villagarcia", "Cardiología",
                        "10+ años de experiencia. Especialista en salud cardiovascular y prevención de enfermedades cardíacas.",
                        "Esteban.png"),
                new MedicoDto("Dr. Keiko Carpio", "Cardiología",
                        "Atención integral en cardiología clínica y procedimientos especializados de alta complejidad.",
                        "Keiko.png"),
                new MedicoDto("Dr. Alan Barrientos", "Cardiología",
                        "Especialista en diagnóstico por imágenes cardiológicas y rehabilitación del paciente crítico.",
                        "DefaultMed.png"),
                new MedicoDto("Dr. Leonidas Frames", "Pediatría",
                        "Cuidado del crecimiento, desarrollo infantil y salud integral de los más pequeños.",
                        "Leonidas.png"),
                new MedicoDto("Dr. Carlos Delgado", "Pediatría",
                        "Especialista en neonatología y seguimiento integral del desarrollo infantil temprano.",
                        "DefaultMed.png"),
                new MedicoDto("Dr. Jeanfranco Lazarinos", "Medicina General",
                        "Consultas generales, controles rutinarios, medicina preventiva y derivación oportuna a especialistas.",
                        "DefaultMed.png"),
                new MedicoDto("Dr. Fernando Lizarraga", "Medicina General",
                        "Enfoque clínico integral enfocado en la atención familiar y control de enfermedades crónicas.",
                        "DefaultMed.png"),
                new MedicoDto("Dra. ByeHello Pauca", "Medicina General",
                        "Atención médica general primaria y evaluaciones de salud ocupacional preventivas.",
                        "DefaultMed.png"),
                new MedicoDto("Dr. Franco Newvillage", "Ginecología",
                        "Salud reproductiva integral, controles ginecológicos periódicos y atención preventiva de la mujer.",
                        "Franco.png"));

        model.addAttribute("listaMedicos", medicos);
        model.addAttribute("totalEspecialistas", medicos.size());

        return "buscar-medicos";
    }

    public static class MedicoDto {
        private final String nombre;
        private final String especialidad;
        private final String bio;
        private final String foto;

        public MedicoDto(String nombre, String especialidad, String bio, String foto) {
            this.nombre = nombre;
            this.especialidad = especialidad;
            this.bio = bio;
            this.foto = foto;
        }

        public String getNombre() {
            return nombre;
        }

        public String getEspecialidad() {
            return especialidad;
        }

        public String getBio() {
            return bio;
        }

        public String getFoto() {
            return foto;
        }
    }

    private String requiereSesion(HttpSession session) {
        return session.getAttribute(SESSION_USUARIO) == null ? "redirect:/login" : null;
    }

    private void flashExito(RedirectAttributes redirectAttributes, String mensaje) {
        redirectAttributes.addFlashAttribute("mensaje", mensaje);
        redirectAttributes.addFlashAttribute("tipoMensaje", "success");
    }

    private void flashError(RedirectAttributes redirectAttributes, String mensaje) {
        redirectAttributes.addFlashAttribute("mensaje", mensaje);
        redirectAttributes.addFlashAttribute("tipoMensaje", "error");
    }

}
