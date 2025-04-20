package com.gestionExamenes.app.controller;

import com.gestionExamenes.app.entidad.Coordinador;
import com.gestionExamenes.app.repositorio.CoordinadorRepositorio;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/coordinador")
public class CoordinadorController {
	
	private CoordinadorRepositorio coordinadorRepositorio;

	// GET: Obtener datos del coordinador autenticado para la UI
    @GetMapping("/profile")
    public ResponseEntity<Map<String, String>> getCoordinatorProfile(Authentication authentication) {
        String documentNumber = authentication.getName(); // documentNumber del usuario autenticado
        Coordinador coordinator = coordinadorRepositorio.findByDocumento(documentNumber);
        if (coordinator == null) {
            return ResponseEntity.notFound().build();
        }
        Map<String, String> profile = new HashMap<>();
        profile.put("documentNumber", coordinator.getDocumento());
        profile.put("role", coordinator.getRol());
        return ResponseEntity.ok(profile);
    }

    // SET: Actualizar preferencias del coordinador (ejemplo: tema de la UI)
    @PostMapping("/preferences")
    public ResponseEntity<Map<String, String>> setPreferences(
            @RequestBody Map<String, String> preferences,
            Authentication authentication) {
        String documentNumber = authentication.getName();
        Coordinador coordinator = coordinadorRepositorio.findByDocumento(documentNumber);
        if (coordinator == null) {
            return ResponseEntity.notFound().build();
        }
        // Aquí podrías guardar preferencias en una colección separada o en el documento del coordinador
        Map<String, String> response = new HashMap<>();
        response.put("message", "Preferencias actualizadas: " + preferences.getOrDefault("theme", "default"));
        return ResponseEntity.ok(response);
    }

    // GET: Obtener estadísticas para la UI (ejemplo: número de estudiantes)
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getCoordinatorStats() {
        long coordinatorCount = coordinadorRepositorio.count();
        Map<String, Long> stats = new HashMap<>();
        stats.put("coordinators", coordinatorCount);
        return ResponseEntity.ok(stats);
    }
}
