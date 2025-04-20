package com.gestionExamenes.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.gestionExamenes.app.entidad.Coordinador;
import com.gestionExamenes.app.repositorio.CoordinadorRepositorio;

@Component
public class CargarCoordinadorInicial implements CommandLineRunner {

    @Autowired
    private CoordinadorRepositorio coordinadorRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verificar si el coordinador ya existe
        if (coordinadorRepositorio.findByDocumento("123456789") == null) {
            Coordinador coordinator = new Coordinador();
            coordinator.setDocumento("123456789");
            coordinator.setPassword(passwordEncoder.encode("admin123"));
            coordinator.setRol("COORDINATOR");
            coordinadorRepositorio.save(coordinator);
            System.out.println("Coordinador creado: documento=123456789, contraseña=admin123");
        } else {
            System.out.println("Coordinador con documento 123456789 ya existe");
        }
    }
}