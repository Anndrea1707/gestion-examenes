package com.gestionExamenes.app.controller;

import com.gestionExamenes.app.entidad.Estudiante;
import com.gestionExamenes.app.entidad.Examen;
import com.gestionExamenes.app.repositorio.EstudianteRepositorio;
import com.gestionExamenes.app.repositorio.ExamenRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/estudiante")
public class EstudianteController {

    @Autowired
    private EstudianteRepositorio estudianteRepository;

    @Autowired
    private ExamenRepositorio examenRepositorio;

    // Vista: Página principal del estudiante
    @GetMapping("/inicio")
    public String paginaInicio() {
        return "estudiante/inicio"; // Vista: templates/estudiante/inicio.html
    }

    // Vista: Perfil del estudiante
    @GetMapping("/perfil")
    public String mostrarPerfilEstudiante(Authentication authentication, Model model) {
        String documento = authentication.getName();
        Estudiante estudiante = estudianteRepository.findByDocumento(documento);

        if (estudiante == null) {
            return "redirect:/login?error";
        }

        model.addAttribute("estudiante", estudiante);
        return "estudiante/perfil"; // Vista: templates/estudiante/perfil.html
    }

    // REST: Obtener datos del perfil del estudiante autenticado (JSON)
    @GetMapping("/profile")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getStudentProfile(Authentication authentication) {
        String documento = authentication.getName();
        Estudiante estudiante = estudianteRepository.findByDocumento(documento);
        if (estudiante == null) {
            return ResponseEntity.notFound().build();
        }
        Map<String, String> profile = new HashMap<>();
        profile.put("documento", estudiante.getDocumento());
        profile.put("nombreCompleto", estudiante.getNombreCompleto());
        profile.put("correo", estudiante.getCorreo());
        profile.put("numeroReferenciaPrueba", estudiante.getNumeroReferenciaPrueba() != null ? estudiante.getNumeroReferenciaPrueba() : "");
        return ResponseEntity.ok(profile);
    }

    // Vista: Resultados del examen
    @GetMapping("/resultados")
    public String mostrarResultados(Authentication authentication, Model model) {
        String documento = authentication.getName();
        Estudiante estudiante = estudianteRepository.findByDocumento(documento);

        if (estudiante == null || estudiante.getNumeroReferenciaPrueba() == null) {
            model.addAttribute("mensaje", "No tienes una prueba registrada.");
            return "estudiante/resultados";
        }

        Examen examen = examenRepositorio.findByNumeroPrueba(estudiante.getNumeroReferenciaPrueba());
        model.addAttribute("prueba", examen != null ? examen : new Examen());
        return "estudiante/resultados"; // Vista: templates/estudiante/resultados.html
    }

    // REST: Obtener datos del examen del estudiante autenticado (JSON)
    @GetMapping("/test")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStudentTest(Authentication authentication) {
        String documento = authentication.getName();
        Estudiante estudiante = estudianteRepository.findByDocumento(documento);
        if (estudiante == null) {
            return ResponseEntity.notFound().build();
        }
        if (estudiante.getNumeroReferenciaPrueba() == null) {
            return ResponseEntity.status(404).body(Map.of("error", "No tienes una prueba registrada"));
        }

        Examen examen = examenRepositorio.findByNumeroPrueba(estudiante.getNumeroReferenciaPrueba());
        if (examen == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Prueba no encontrada"));
        }

        Map<String, Object> testData = new HashMap<>();
        testData.put("numeroPrueba", examen.getNumeroPrueba());
        testData.put("puntajeTotal", examen.getPuntajeTotal());
        testData.put("nivelPuntajeTotal", examen.getNivelPuntajeTotal());
        testData.put("puntajeComunicacionEscrita", examen.getPuntajeComunicacionEscrita());
        testData.put("nivelPuntajeComunicacionEscrita", examen.getNivelPuntajeComunicacionEscrita());
        testData.put("puntajeRazonamientoCuantitativo", examen.getPuntajeRazonamientoCuantitativo());
        testData.put("nivelPuntajeRazonamientoCuantitativo", examen.getNivelPuntajeRazonamientoCuantitativo());
        testData.put("puntajeLecturaCritica", examen.getPuntajeLecturaCritica());
        testData.put("nivelPuntajeLecturaCritica", examen.getNivelPuntajeLecturaCritica());
        testData.put("puntajeCompetenciasCiudadanas", examen.getPuntajeCompetenciasCiudadanas());
        testData.put("nivelPuntajeCompetenciasCiudadanas", examen.getNivelPuntajeCompetenciasCiudadanas());
        testData.put("puntajeIngles", examen.getPuntajeIngles());
        testData.put("nivelPuntajeIngles", examen.getNivelPuntajeIngles());
        testData.put("nivelDeIngles", examen.getNivelDeIngles());
        testData.put("puntajeFormulacionProyectoIngenieria", examen.getPuntajeFormulacionProyectoIngenieria());
        testData.put("nivelPuntajeFormulacionProyectoIngenieria", examen.getNivelPuntajeFormulacionProyectoIngenieria());
        testData.put("puntajePensamientoCientificoMatematicasEstadistica", examen.getPuntajePensamientoCientificoMatematicasEstadistica());
        testData.put("nivelPuntajePensamientoCientificoMatematicasEstadistica", examen.getNivelPuntajePensamientoCientificoMatematicasEstadistica());
        testData.put("puntajeDisenoSoftware", examen.getPuntajeDisenoSoftware());
        testData.put("nivelPuntajeDisenoSoftware", examen.getNivelPuntajeDisenoSoftware());

        return ResponseEntity.ok(testData);
    }

    // REST: Obtener puntajes totales para ranking
    @GetMapping("/ranking")
    @ResponseBody
    public ResponseEntity<List<Integer>> getAllTotalScores() {
        try {
            List<Estudiante> estudiantes = estudianteRepository.findByNumeroReferenciaPruebaIsNotNull();
            if (estudiantes.isEmpty()) {
                return ResponseEntity.ok(List.of());
            }

            List<Integer> totalScores = estudiantes.stream()
                    .map(estudiante -> examenRepositorio.findByNumeroPrueba(estudiante.getNumeroReferenciaPrueba()))
                    .filter(examen -> examen != null)
                    .map(Examen::getPuntajeTotal)
                    .filter(score -> score > 0) // Opcional: filtrar puntajes no válidos
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(totalScores);
        } catch (Exception e) {
            System.out.println("Error al obtener puntajes totales: " + e.getMessage());
            return ResponseEntity.status(500).body(List.of());
        }
    }
}