package com.lasalle.medigest.presentacion;

import jakarta.servlet.http.HttpSession; // IMPORTANTE: Para manejar la sesión
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainUiController {

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
    public String dashboard(HttpSession session) { // Pedimos la sesión aquí también

        // VERIFICACIÓN: Si la marca no existe, significa que no ha pasado por el login
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login"; // Bloqueado, regresa al login
        }

        // Si existe, lo dejamos ver el dashboard felizmente
        return "dashboard";
    }

    @GetMapping("/dashboard/buscar-medicos")
    public String buscarMedicos(HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }
        return "buscar-medicos"; // Crearemos buscar-medicos.html a continuación
    }

    // NUEVA RUTA: Historial Clínico
    @GetMapping("/dashboard/historial-clinico")
    public String historialClinico(HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }
        return "historial"; // Crearemos historial.html a continuación
    }

    // BONUS: Para cuando le dé click a "Cerrar Sesión" en tu menú lateral
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Borra todo lo de la sesión
        return "redirect:/"; // Regresa a la landing page
    }
}