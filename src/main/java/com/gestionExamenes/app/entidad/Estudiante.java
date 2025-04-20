package com.gestionExamenes.app.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "estudiantes")
public class Estudiante {

    @Id
    private String id;
    private String nombreCompleto;
    private String documento; // Documento de identidad para iniciar sesión
    private String contrasena;
    private String correo;
    private String numeroReferenciaPrueba; // Relacionado al examen

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNumeroReferenciaPrueba() {
        return numeroReferenciaPrueba;
    }

    public void setNumeroReferenciaPrueba(String numeroReferenciaPrueba) {
        this.numeroReferenciaPrueba = numeroReferenciaPrueba;
    }
}
