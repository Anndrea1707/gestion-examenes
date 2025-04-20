package com.gestionExamenes.app.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.gestionExamenes.app.entidad.Coordinador;

public interface CoordinadorRepositorio extends MongoRepository<Coordinador, String> {
    Coordinador findByDocumento(String documento); // para login
}
