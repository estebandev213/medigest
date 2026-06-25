package com.lasalle.medigest.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainUiController {

    // Redirige la raíz (http://localhost:8080/) directo al login
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // Ruta para el Login (templates/login.html)
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Ruta para el Dashboard (templates/dashboard.html)
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
}