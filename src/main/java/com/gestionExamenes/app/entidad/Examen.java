package com.gestionExamenes.app.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "examenes")
public class Examen {

    @Id
    private String id;
    private String numeroPrueba; // Relacionado con el estudiante
    private int puntajeTotal;
    private String nivelPuntajeTotal;
    
    private int puntajeComunicacionEscrita;
    private String nivelPuntajeComunicacionEscrita;
    
    private int puntajeRazonamientoCuantitativo;
    private String nivelPuntajeRazonamientoCuantitativo;
    
    private int puntajeLecturaCritica;
    private String nivelPuntajeLecturaCritica;
    
    private int puntajeCompetenciasCiudadanas;
    private String nivelPuntajeCompetenciasCiudadanas;
    
    private int puntajeIngles;  // Puntaje en inglés
    private String nivelPuntajeIngles;  // Nivel basado en puntaje (1, 2, o 3)
    private String nivelDeIngles;  // Nivel de inglés según el estándar (A0, A1, A2, B1, B2, etc.)
    
    private int puntajeFormulacionProyectoIngenieria;
    private String nivelPuntajeFormulacionProyectoIngenieria;
    
    private int puntajePensamientoCientificoMatematicasEstadistica;
    private String nivelPuntajePensamientoCientificoMatematicasEstadistica;
    
    private int puntajeDisenoSoftware;
    private String nivelPuntajeDisenoSoftware;

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumeroPrueba() {
        return numeroPrueba;
    }

    public void setNumeroPrueba(String numeroPrueba) {
        this.numeroPrueba = numeroPrueba;
    }

    public int getPuntajeTotal() {
        return puntajeTotal;
    }

    public void setPuntajeTotal(int puntajeTotal) {
        this.puntajeTotal = puntajeTotal;
    }

    public String getNivelPuntajeTotal() {
        return nivelPuntajeTotal;
    }

    public void setNivelPuntajeTotal(String nivelPuntajeTotal) {
        this.nivelPuntajeTotal = nivelPuntajeTotal;
    }

    public int getPuntajeComunicacionEscrita() {
        return puntajeComunicacionEscrita;
    }

    public void setPuntajeComunicacionEscrita(int puntajeComunicacionEscrita) {
        this.puntajeComunicacionEscrita = puntajeComunicacionEscrita;
    }

    public String getNivelPuntajeComunicacionEscrita() {
        return nivelPuntajeComunicacionEscrita;
    }

    public void setNivelPuntajeComunicacionEscrita(String nivelPuntajeComunicacionEscrita) {
        this.nivelPuntajeComunicacionEscrita = nivelPuntajeComunicacionEscrita;
    }

    public int getPuntajeRazonamientoCuantitativo() {
        return puntajeRazonamientoCuantitativo;
    }

    public void setPuntajeRazonamientoCuantitativo(int puntajeRazonamientoCuantitativo) {
        this.puntajeRazonamientoCuantitativo = puntajeRazonamientoCuantitativo;
    }

    public String getNivelPuntajeRazonamientoCuantitativo() {
        return nivelPuntajeRazonamientoCuantitativo;
    }

    public void setNivelPuntajeRazonamientoCuantitativo(String nivelPuntajeRazonamientoCuantitativo) {
        this.nivelPuntajeRazonamientoCuantitativo = nivelPuntajeRazonamientoCuantitativo;
    }

    public int getPuntajeLecturaCritica() {
        return puntajeLecturaCritica;
    }

    public void setPuntajeLecturaCritica(int puntajeLecturaCritica) {
        this.puntajeLecturaCritica = puntajeLecturaCritica;
    }

    public String getNivelPuntajeLecturaCritica() {
        return nivelPuntajeLecturaCritica;
    }

    public void setNivelPuntajeLecturaCritica(String nivelPuntajeLecturaCritica) {
        this.nivelPuntajeLecturaCritica = nivelPuntajeLecturaCritica;
    }

    public int getPuntajeCompetenciasCiudadanas() {
        return puntajeCompetenciasCiudadanas;
    }

    public void setPuntajeCompetenciasCiudadanas(int puntajeCompetenciasCiudadanas) {
        this.puntajeCompetenciasCiudadanas = puntajeCompetenciasCiudadanas;
    }

    public String getNivelPuntajeCompetenciasCiudadanas() {
        return nivelPuntajeCompetenciasCiudadanas;
    }

    public void setNivelPuntajeCompetenciasCiudadanas(String nivelPuntajeCompetenciasCiudadanas) {
        this.nivelPuntajeCompetenciasCiudadanas = nivelPuntajeCompetenciasCiudadanas;
    }

    public int getPuntajeIngles() {
        return puntajeIngles;
    }

    public void setPuntajeIngles(int puntajeIngles) {
        this.puntajeIngles = puntajeIngles;
    }

    public String getNivelPuntajeIngles() {
        return nivelPuntajeIngles;
    }

    public void setNivelPuntajeIngles(String nivelPuntajeIngles) {
        this.nivelPuntajeIngles = nivelPuntajeIngles;
    }

    public String getNivelDeIngles() {
        return nivelDeIngles;
    }

    public void setNivelDeIngles(String nivelDeIngles) {
        this.nivelDeIngles = nivelDeIngles;
    }

    public int getPuntajeFormulacionProyectoIngenieria() {
        return puntajeFormulacionProyectoIngenieria;
    }

    public void setPuntajeFormulacionProyectoIngenieria(int puntajeFormulacionProyectoIngenieria) {
        this.puntajeFormulacionProyectoIngenieria = puntajeFormulacionProyectoIngenieria;
    }

    public String getNivelPuntajeFormulacionProyectoIngenieria() {
        return nivelPuntajeFormulacionProyectoIngenieria;
    }

    public void setNivelPuntajeFormulacionProyectoIngenieria(String nivelPuntajeFormulacionProyectoIngenieria) {
        this.nivelPuntajeFormulacionProyectoIngenieria = nivelPuntajeFormulacionProyectoIngenieria;
    }

    public int getPuntajePensamientoCientificoMatematicasEstadistica() {
        return puntajePensamientoCientificoMatematicasEstadistica;
    }

    public void setPuntajePensamientoCientificoMatematicasEstadistica(int puntajePensamientoCientificoMatematicasEstadistica) {
        this.puntajePensamientoCientificoMatematicasEstadistica = puntajePensamientoCientificoMatematicasEstadistica;
    }

    public String getNivelPuntajePensamientoCientificoMatematicasEstadistica() {
        return nivelPuntajePensamientoCientificoMatematicasEstadistica;
    }

    public void setNivelPuntajePensamientoCientificoMatematicasEstadistica(String nivelPuntajePensamientoCientificoMatematicasEstadistica) {
        this.nivelPuntajePensamientoCientificoMatematicasEstadistica = nivelPuntajePensamientoCientificoMatematicasEstadistica;
    }

    public int getPuntajeDisenoSoftware() {
        return puntajeDisenoSoftware;
    }

    public void setPuntajeDisenoSoftware(int puntajeDisenoSoftware) {
        this.puntajeDisenoSoftware = puntajeDisenoSoftware;
    }

    public String getNivelPuntajeDisenoSoftware() {
        return nivelPuntajeDisenoSoftware;
    }

    public void setNivelPuntajeDisenoSoftware(String nivelPuntajeDisenoSoftware) {
        this.nivelPuntajeDisenoSoftware = nivelPuntajeDisenoSoftware;
    }
}
