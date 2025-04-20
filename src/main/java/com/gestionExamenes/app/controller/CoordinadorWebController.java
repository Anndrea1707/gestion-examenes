package com.gestionExamenes.app.controller;

import com.gestionExamenes.app.entidad.Coordinador;
import com.gestionExamenes.app.repositorio.CoordinadorRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coordinadores")
public class CoordinadorWebController {

    @Autowired
    private CoordinadorRepositorio coordinadorRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Listar todos los coordinadores
    @GetMapping
    public List<Coordinador> getAllCoordinators() {
        return coordinadorRepositorio.findAll();
    }

    // Obtener un coordinador por documentNumber
    @GetMapping("/{documentNumber}")
    public ResponseEntity<Coordinador> getCoordinatorByDocument(@PathVariable String documentNumber) {
    	Coordinador coordinator = coordinadorRepositorio.findByDocumento(documentNumber);
        if (coordinator != null) {
            return ResponseEntity.ok(coordinator);
        }
        return ResponseEntity.notFound().build();
    }

    // Crear un nuevo coordinador
    @PostMapping
    public ResponseEntity<Coordinador> createCoordinator(@RequestBody Coordinador coordinator) {
        if (coordinadorRepositorio.findByDocumento(coordinator.getDocumento()) != null) {
            return ResponseEntity.badRequest().build();
        }
        coordinator.setPassword(passwordEncoder.encode(coordinator.getPassword()));
        coordinator.setRol("COORDINATOR");
        Coordinador saved = coordinadorRepositorio.save(coordinator);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{documentNumber}")
    public ResponseEntity<Coordinador> updateCoordinator(@PathVariable String documentNumber, @RequestBody Coordinador coordinator) {
        Coordinador existing = coordinadorRepositorio.findByDocumento(documentNumber);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        coordinator.setId(existing.getId());
        if (coordinator.getPassword() != null && !coordinator.getPassword().isEmpty()) {
            coordinator.setPassword(passwordEncoder.encode(coordinator.getPassword()));
        } else {
            coordinator.setPassword(existing.getPassword());
        }
        coordinator.setRol("COORDINATOR");
        Coordinador updated = coordinadorRepositorio.save(coordinator);
        return ResponseEntity.ok(updated);
    }

    // Eliminar un coordinador
    @DeleteMapping("/{documentNumber}")
    public ResponseEntity<Void> deleteCoordinator(@PathVariable String documentNumber) {
    	Coordinador coordinator = coordinadorRepositorio.findByDocumento(documentNumber);
        if (coordinator != null) {
        	coordinadorRepositorio.delete(coordinator);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
