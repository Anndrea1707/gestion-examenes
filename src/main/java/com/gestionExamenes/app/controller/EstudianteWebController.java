package com.gestionExamenes.app.controller;

import com.gestionExamenes.app.entidad.Estudiante;
import com.gestionExamenes.app.entidad.Examen;
import com.gestionExamenes.app.repositorio.EstudianteRepositorio;
import com.gestionExamenes.app.repositorio.ExamenRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/estudiantes")
public class EstudianteWebController {

    @Autowired
    private EstudianteRepositorio estudianteRepository;

    @Autowired
    private ExamenRepositorio examenRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Función para calcular el nivel según el puntaje
    private String calcularNivel(int puntaje) {
        if (puntaje >= 191 && puntaje <= 300) {
            return "Nivel 4";
        } else if (puntaje >= 156 && puntaje <= 190) {
            return "Nivel 3";
        } else if (puntaje >= 126 && puntaje <= 155) {
            return "Nivel 2";
        } else if (puntaje >= 0 && puntaje <= 125) {
            return "Nivel 1";
        } else {
            return "Error";
        }
    }

    // Vista: Listar todos los estudiantes
    @GetMapping
    public String listStudents() {
        return "redirect:/estudiantes.html";
    }

    // Vista: Formulario para nuevo estudiante
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        return "redirect:/registroEstudiante.html";
    }

    // Vista: Guardar nuevo estudiante
    @PostMapping
    public String createStudent(@ModelAttribute Estudiante student) {
        if (estudianteRepository.findByDocumento(student.getDocumento()) != null) {
            return "redirect:/estudiantes/new?error=documentoExistente";
        }
        if (!student.getCorreo().endsWith("@uts.edu.co")) {
            return "redirect:/estudiantes/new?error=correoInvalido";
        }
        if (!student.getContrasena().matches("^(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$")) {
            return "redirect:/estudiantes/new?error=contrasenaInvalida";
        }
        student.setContrasena(passwordEncoder.encode(student.getContrasena()));
        student.setNumeroReferenciaPrueba(null);
        estudianteRepository.save(student);
        return "redirect:/estudiantes";
    }

    // Vista: Formulario para editar estudiante
    @GetMapping("/edit/{documento}")
    public String showEditForm(@PathVariable String documento, Model model) {
        Estudiante student = estudianteRepository.findByDocumento(documento);
        if (student == null) {
            return "redirect:/estudiantes?notfound";
        }
        return "redirect:/editarEstudiante.html?documento=" + documento;
    }

    // Vista: Actualizar estudiante
    @PostMapping("/update/{documento}")
    public String updateStudent(@PathVariable String documento, @ModelAttribute Estudiante student) {
        Estudiante existing = estudianteRepository.findByDocumento(documento);
        if (existing == null) {
            return "redirect:/editarEstudiante.html?documento=" + documento + "&error=notfound";
        }
        student.setId(existing.getId());
        if (student.getContrasena() != null && !student.getContrasena().isEmpty()) {
            if (!student.getContrasena().matches("^(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$")) {
                return "redirect:/editarEstudiante.html?documento=" + documento + "&error=contrasenaInvalida";
            }
            student.setContrasena(passwordEncoder.encode(student.getContrasena()));
        } else {
            student.setContrasena(existing.getContrasena());
        }
        if (!student.getCorreo().endsWith("@uts.edu.co")) {
            return "redirect:/editarEstudiante.html?documento=" + documento + "&error=correoInvalido";
        }
        estudianteRepository.save(student);
        return "redirect:/estudiantes";
    }

    // Vista: Ver prueba de estudiante
    @GetMapping("/view-test/{documento}")
    public String viewTest(@PathVariable String documento) {
        return "redirect:/verPrueba.html?documento=" + documento;
    }

    // API: Obtener todos los estudiantes
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<Estudiante>> getAllStudents() {
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        return ResponseEntity.ok(estudiantes);
    }

    // API: Obtener estudiante por documento
    @GetMapping("/api/{documento}")
    @ResponseBody
    public ResponseEntity<Estudiante> getStudentByDocument(@PathVariable String documento) {
        System.out.println("Buscando estudiante con documento: " + documento);
        Estudiante estudiante = estudianteRepository.findByDocumento(documento);
        if (estudiante != null) {
            System.out.println("Estudiante encontrado: " + estudiante);
            return ResponseEntity.ok(estudiante);
        }
        System.out.println("Estudiante no encontrado para documento: " + documento);
        return ResponseEntity.notFound().build();
    }

    // API: Obtener estudiantes sin prueba
    @GetMapping("/api/without-test")
    @ResponseBody
    public ResponseEntity<List<Estudiante>> getStudentsWithoutTest() {
        List<Estudiante> estudiantes = estudianteRepository.findByNumeroReferenciaPruebaIsNull();
        return ResponseEntity.ok(estudiantes);
    }

    // API: Registrar prueba para un estudiante
    @PostMapping("/{documento}/test")
    public String registerTest(@PathVariable String documento, 
                              @ModelAttribute Examen examen, 
                              @RequestParam("puntajeTotalStr") String puntajeTotalStr) {
        Estudiante estudiante = estudianteRepository.findByDocumento(documento);
        if (estudiante == null) {
            return "redirect:/registrarPrueba.html?error=estudianteNoEncontrado";
        }
        if (estudiante.getNumeroReferenciaPrueba() != null) {
            return "redirect:/registrarPrueba.html?error=estudianteYaTienePrueba";
        }

        boolean esAnulado = puntajeTotalStr.trim().equalsIgnoreCase("Anulado");

        if (esAnulado) {
            // Configurar valores para prueba anulada
            examen.setPuntajeTotal(0);
            examen.setNivelPuntajeTotal("No aplica");
            examen.setPuntajeComunicacionEscrita(0);
            examen.setNivelPuntajeComunicacionEscrita("No aplica");
            examen.setPuntajeRazonamientoCuantitativo(0);
            examen.setNivelPuntajeRazonamientoCuantitativo("No aplica");
            examen.setPuntajeLecturaCritica(0);
            examen.setNivelPuntajeLecturaCritica("No aplica");
            examen.setPuntajeCompetenciasCiudadanas(0);
            examen.setNivelPuntajeCompetenciasCiudadanas("No aplica");
            examen.setPuntajeIngles(0);
            examen.setNivelPuntajeIngles("No aplica");
            examen.setNivelDeIngles("");
            examen.setPuntajeFormulacionProyectoIngenieria(0);
            examen.setNivelPuntajeFormulacionProyectoIngenieria("No aplica");
            examen.setPuntajePensamientoCientificoMatematicasEstadistica(0);
            examen.setNivelPuntajePensamientoCientificoMatematicasEstadistica("No aplica");
            examen.setPuntajeDisenoSoftware(0);
            examen.setNivelPuntajeDisenoSoftware("No aplica");
        } else {
            // Calcular niveles para puntajes válidos
            try {
                int puntajeTotal = Integer.parseInt(puntajeTotalStr);
                if (puntajeTotal < 0) {
                    return "redirect:/registrarPrueba.html?error=puntajeTotalInvalido";
                }
                examen.setPuntajeTotal(puntajeTotal);
                examen.setNivelPuntajeTotal(calcularNivel(puntajeTotal));
            } catch (NumberFormatException e) {
                return "redirect:/registrarPrueba.html?error=puntajeTotalInvalido";
            }
            // Validar que los demás puntajes no sean negativos
            if (examen.getPuntajeComunicacionEscrita() < 0 ||
                examen.getPuntajeRazonamientoCuantitativo() < 0 ||
                examen.getPuntajeLecturaCritica() < 0 ||
                examen.getPuntajeCompetenciasCiudadanas() < 0 ||
                examen.getPuntajeIngles() < 0 ||
                examen.getPuntajeFormulacionProyectoIngenieria() < 0 ||
                examen.getPuntajePensamientoCientificoMatematicasEstadistica() < 0 ||
                examen.getPuntajeDisenoSoftware() < 0) {
                return "redirect:/registrarPrueba.html?error=puntajesInvalidos";
            }
            examen.setNivelPuntajeComunicacionEscrita(calcularNivel(examen.getPuntajeComunicacionEscrita()));
            examen.setNivelPuntajeRazonamientoCuantitativo(calcularNivel(examen.getPuntajeRazonamientoCuantitativo()));
            examen.setNivelPuntajeLecturaCritica(calcularNivel(examen.getPuntajeLecturaCritica()));
            examen.setNivelPuntajeCompetenciasCiudadanas(calcularNivel(examen.getPuntajeCompetenciasCiudadanas()));
            examen.setNivelPuntajeIngles(calcularNivel(examen.getPuntajeIngles()));
            // nivelDeIngles ya está seteado por el usuario
            examen.setNivelPuntajeFormulacionProyectoIngenieria(calcularNivel(examen.getPuntajeFormulacionProyectoIngenieria()));
            examen.setNivelPuntajePensamientoCientificoMatematicasEstadistica(calcularNivel(examen.getPuntajePensamientoCientificoMatematicasEstadistica()));
            examen.setNivelPuntajeDisenoSoftware(calcularNivel(examen.getPuntajeDisenoSoftware()));
        }

        examenRepositorio.save(examen);
        estudiante.setNumeroReferenciaPrueba(examen.getNumeroPrueba());
        estudianteRepository.save(estudiante);
        return "redirect:/estudiantes";
    }

    // API: Obtener prueba por número
    @GetMapping("/examenes/api/numero/{numeroPrueba}")
    @ResponseBody
    public ResponseEntity<Examen> getExamenByNumero(@PathVariable String numeroPrueba) {
        System.out.println("Buscando examen con numeroPrueba: " + numeroPrueba);
        Examen examen = examenRepositorio.findByNumeroPrueba(numeroPrueba);
        if (examen != null) {
            System.out.println("Examen encontrado: " + examen.getNumeroPrueba());
            return ResponseEntity.ok(examen);
        }
        System.out.println("Examen no encontrado: " + numeroPrueba);
        return ResponseEntity.notFound().build();
    }

 // Endpoint para datos personales del estudiante autenticado
    @GetMapping("/estudiante/api/personal")
    @ResponseBody
    public ResponseEntity<Estudiante> getPersonalInfo() {
        String documento = getAuthenticatedDocumento();
        System.out.println("Buscando datos personales para documento: " + documento);
        Estudiante estudiante = estudianteRepository.findByDocumento(documento);
        if (estudiante != null) {
            System.out.println("Estudiante encontrado: " + estudiante.getDocumento());
            return ResponseEntity.ok(estudiante);
        }
        System.out.println("Estudiante no encontrado: " + documento);
        return ResponseEntity.notFound().build();
    }

    // Endpoint para información de la prueba del estudiante autenticado
    @GetMapping("/estudiante/api/examen")
    @ResponseBody
    public ResponseEntity<Estudiante> getExamInfo() {
        String documento = getAuthenticatedDocumento();
        System.out.println("Buscando datos de examen para documento: " + documento);
        Estudiante estudiante = estudianteRepository.findByDocumento(documento);
        if (estudiante != null) {
            System.out.println("Estudiante encontrado: " + estudiante.getDocumento());
            return ResponseEntity.ok(estudiante);
        }
        System.out.println("Estudiante no encontrado: " + documento);
        return ResponseEntity.notFound().build();
    }

    // Endpoint para el ranking global
    @GetMapping("/estudiante/api/ranking")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getRanking() {
        String documento = getAuthenticatedDocumento();
        System.out.println("Calculando ranking para documento: " + documento);
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        List<Examen> examenes = examenRepositorio.findAll();

        // Mapear estudiantes con sus puntajes
        List<Map<String, Object>> estudiantesConPuntaje = estudiantes.stream()
            .filter(e -> e.getNumeroReferenciaPrueba() != null)
            .map(e -> {
                Examen examen = examenes.stream()
                    .filter(ex -> ex.getNumeroPrueba().equals(e.getNumeroReferenciaPrueba()))
                    .findFirst()
                    .orElse(null);
                Map<String, Object> map = new HashMap<>();
                map.put("documento", e.getDocumento());
                map.put("nombreCompleto", e.getNombreCompleto());
                map.put("puntajeTotal", examen != null && !examen.getNivelPuntajeTotal().equals("No aplica") ? examen.getPuntajeTotal() : 0);
                return map;
            })
            .filter(map -> (int) map.get("puntajeTotal") > 0)
            .sorted(Comparator.comparingInt(m -> -(int) m.get("puntajeTotal")))
            .collect(Collectors.toList());

        // Encontrar la posición del estudiante autenticado
        int posicion = 0;
        for (int i = 0; i < estudiantesConPuntaje.size(); i++) {
            if (estudiantesConPuntaje.get(i).get("documento").equals(documento)) {
                posicion = i + 1;
                break;
            }
        }

        // Obtener los 3 mejores
        List<Map<String, Object>> top3 = estudiantesConPuntaje.stream()
            .limit(3)
            .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("posicion", posicion > 0 ? posicion : "No disponible");
        response.put("top3", top3);

        System.out.println("Ranking calculado: posicion=" + response.get("posicion") + ", top3=" + top3);
        return ResponseEntity.ok(response);
    }

    // Obtener el documento del usuario autenticado
    private String getAuthenticatedDocumento() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername(); // username = documento
        }
        return principal.toString();
    }
}