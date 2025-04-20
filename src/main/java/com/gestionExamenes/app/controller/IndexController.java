package com.gestionExamenes.app.controller;

import com.gestionExamenes.app.entidad.Coordinador;
import com.gestionExamenes.app.entidad.Estudiante;
import com.gestionExamenes.app.repositorio.CoordinadorRepositorio;
import com.gestionExamenes.app.repositorio.EstudianteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @Autowired
    private CoordinadorRepositorio coordinadorRepositorio;

    @Autowired
    private EstudianteRepositorio estudianteRepositorio;

    @GetMapping({"/", "/index"})
    public String redirectToHomePage() {
        return "redirect:/index.html";
    }

    @GetMapping({"/cuentas"})
    public String redirectToAccounts() {
        return "redirect:/cuentas.html"; // Redirige al HTML estático
    }

    @GetMapping("/api/cuentas")
    @ResponseBody
    public Map<String, List<?>> getAllAccounts() {
        List<Coordinador> coordinadores = coordinadorRepositorio.findAll();
        List<Estudiante> estudiantes = estudianteRepositorio.findAll();
        System.out.println("Coordinadores encontrados: " + coordinadores.size());
        System.out.println("Estudiantes encontrados: " + estudiantes.size());
        Map<String, List<?>> response = new HashMap<>();
        response.put("coordinadores", coordinadores);
        response.put("estudiantes", estudiantes);
        return response;
    }

    @GetMapping({"/login"})
    public String redirectToLogin() {
        return "redirect:/login.html";
    }

    @GetMapping({"/registroEstudiante"})
    public String redirectToRegister() {
        return "redirect:/registroEstudiante.html";
    }

    @GetMapping("/api/user")
    @ResponseBody
    public Map<String, String> getUserRole(Authentication authentication) {
        Map<String, String> response = new HashMap<>();
        if (authentication != null && authentication.isAuthenticated()) {
            String role = authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
            response.put("role", role);
        } else {
            response.put("role", "ANONYMOUS");
        }
        return response;
    }
}