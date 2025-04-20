package com.gestionExamenes.app.repositorio;

import com.gestionExamenes.app.entidad.Estudiante;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudianteRepositorio extends MongoRepository<Estudiante, String> {

    // Buscar estudiante por documento
    Estudiante findByDocumento(String documento);
    
    @Query("{ 'numeroReferenciaPrueba': null }")
    List<Estudiante> findByNumeroReferenciaPruebaIsNull();
    
    @Query("{ 'numeroReferenciaPrueba': { $ne: null } }")
    List<Estudiante> findByNumeroReferenciaPruebaIsNotNull();
}
