package com.lasalle.medigest.presentacion;

import jakarta.servlet.http.HttpSession; // IMPORTANTE: Para manejar la sesión

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lasalle.medigest.aplicacion.admision.ServicioAdmision;
import com.lasalle.medigest.aplicacion.citas.ServicioCitas;
import com.lasalle.medigest.dominio.admision.Paciente;
import com.lasalle.medigest.dominio.admision.TipoPaciente;

@Controller
public class MainUiController {

    @Autowired
    private ServicioCitas servicioCitas; // Inyectamos el servicio que maneja las citas

    @Autowired
    private ServicioAdmision servicioAdmision; // Inyectamos el servicio real de Admisión

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
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session, // Inyectamos la sesión aquí
            Model model) {

        if ("admin".equals(username) && "admin".equals(password)) {
            // Guardamos una marca en la sesión para saber que YA se logueó
            session.setAttribute("usuarioLogueado", username);
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Usuario o contraseña incorrectos.");
            return "login";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // CONTROL DE ACCESO: Si no está logueado, vuelve al login
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }

        // Forzamos a traer la lista de citas
        var listaCitas = servicioCitas.listarTodas();

        System.out.println("==================================================");
        System.out.println("DEBUG MEDIGEST: ¡FORZANDO ENTRADA A PANTALLA!");
        System.out.println("Cantidad de elementos: " + (listaCitas != null ? listaCitas.size() : "NULO"));
        System.out.println("==================================================");

        model.addAttribute("misCitas", listaCitas);
        model.addAttribute("totalCitas", listaCitas != null ? listaCitas.size() : 0);

        return "dashboard";
    }

    @PostMapping("/dashboard/programar-cita")
    public String programarCita(@RequestParam Long pacienteId, // CORREGIDO: Recibe directamente el ID de la base de
                                                               // datos
            @RequestParam String medicoAsignado,
            @RequestParam String especialidad,
            @RequestParam String fechaHora,
            @RequestParam String motivo) {

        // DEBUG para ver los datos limpios en tu consola de Spring Boot
        System.out.println("======= NUEVA CITA RECIBIDA =======");
        System.out.println("PACIENTE_ID (Código): " + pacienteId);
        System.out.println("Médico: " + medicoAsignado);
        System.out.println("Especialidad: " + especialidad);
        System.out.println("Fecha/Hora: " + fechaHora);
        System.out.println("Motivo: " + motivo);
        System.out.println("===================================");

        // Aquí la gente de backend ya lo tiene directo para pasar al Módulo 2 sin
        // mapeos extra:
        /*
         * Cita nuevaCita = new Cita();
         * Armar aqui el constructor o la consulta para crear la cita con los datos
         * recibidos
         * 
         * servicioCitas.programar(nuevaCita);
         */

        return "redirect:/dashboard";
    }

@GetMapping("/dashboard/pacientes")
    public String listarPacientes(HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }

        System.out.println("DEBUG: Extrayendo pacientes mediante el módulo ServicioAdmision...");

        // Consumimos el método real del servicio de admisión
        var listaReal = servicioAdmision.listarTodos(); 
        
        model.addAttribute("listaPacientes", listaReal);
        model.addAttribute("totalPacientes", listaReal != null ? listaReal.size() : 0);

        return "pacientes"; 
    }

    @PostMapping("/dashboard/pacientes/registrar")
    public String registrarPaciente(@RequestParam String dni,
                                    @RequestParam String nombres,
                                    @RequestParam String apellidos,
                                    @RequestParam String telefono,
                                    @RequestParam String direccion,
                                    @RequestParam String email,
                                    @RequestParam String fechaNacimiento,
                                    @RequestParam String tipo) {
        try {
            System.out.println("DEBUG: Convirtiendo el String tipo a TipoPaciente Enum...");
            // SOLUCIÓN AL ERROR DE TIPO: Convertimos el String del formulario ("SIS" o "PARTICULAR") al Enum real
            TipoPaciente tipoEnum = TipoPaciente.valueOf(tipo.toUpperCase());

            // SOLUCIÓN AL REGISTRAR: Construimos el objeto usando el Builder de Lombok que tiene tu entidad
            Paciente nuevoPaciente = Paciente.builder()
                    .dni(dni)
                    .nombres(nombres)
                    .apellidos(apellidos)
                    .telefono(telefono)
                    .direccion(direccion)
                    .email(email)
                    .fechaNacimiento(fechaNacimiento) // Como en tu entidad es String, pasa directo limpio
                    .tipo(tipoEnum) // Pasamos el Enum mapeado correctamente
                    .build();

            // Enviamos el objeto construido al backend para que impacte la BD h2
            servicioAdmision.registrarPaciente(nuevoPaciente);
            System.out.println("DEBUG: ¡Paciente registrado con éxito en la Base de Datos!");

        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: El tipo de seguro enviado no coincide con el Enum TipoPaciente: " + tipo);
        } catch (Exception e) {
            System.out.println("ERROR general al registrar en ServicioAdmision: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/dashboard/pacientes";
    }

    @GetMapping("/dashboard/buscar-medicos")
    public String buscarMedicos(HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }
        return "buscar-medicos";
    }

    @GetMapping("/dashboard/historial-clinico")
    public String historialClinico(HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }
        return "historial";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}