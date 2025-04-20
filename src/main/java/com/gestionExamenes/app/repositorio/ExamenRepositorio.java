package com.gestionExamenes.app.repositorio;

import com.gestionExamenes.app.entidad.Examen;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamenRepositorio extends MongoRepository<Examen, String> {
    Examen findByNumeroPrueba(String numeroPrueba);
}