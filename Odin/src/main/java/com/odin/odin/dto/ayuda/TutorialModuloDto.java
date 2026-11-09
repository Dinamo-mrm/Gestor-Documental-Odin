package com.odin.odin.dto.ayuda;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa el tutorial interactivo de un módulo funcional real de ODIN.
 */
public class TutorialModuloDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String slug;
    private String titulo;
    private String moduloNombre;
    private String categoria;
    private String descripcionCorta;
    private String descripcionDetallada;
    private String icono; // Identificador de icono o SVG
    private String colorBadge;
    private String videoUrl;
    private String embedVideoUrl;
    private String objetivo;
    private List<String> funcionalidadesExplicadas = new ArrayList<>();
    private List<PasoGuiaDto> pasos = new ArrayList<>();
    private List<String> palabrasClave = new ArrayList<>();
    private String rutaSistema; // URL de la vista real en ODIN (ej: /view/radicados)

    public TutorialModuloDto() {
    }

    public boolean isTieneVideo() {
        return embedVideoUrl != null && !embedVideoUrl.trim().isEmpty();
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getModuloNombre() {
        return moduloNombre;
    }

    public void setModuloNombre(String moduloNombre) {
        this.moduloNombre = moduloNombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcionCorta() {
        return descripcionCorta;
    }

    public void setDescripcionCorta(String descripcionCorta) {
        this.descripcionCorta = descripcionCorta;
    }

    public String getDescripcionDetallada() {
        return descripcionDetallada;
    }

    public void setDescripcionDetallada(String descripcionDetallada) {
        this.descripcionDetallada = descripcionDetallada;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getColorBadge() {
        return colorBadge;
    }

    public void setColorBadge(String colorBadge) {
        this.colorBadge = colorBadge;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getEmbedVideoUrl() {
        return embedVideoUrl;
    }

    public void setEmbedVideoUrl(String embedVideoUrl) {
        this.embedVideoUrl = embedVideoUrl;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public List<String> getFuncionalidadesExplicadas() {
        return funcionalidadesExplicadas;
    }

    public void setFuncionalidadesExplicadas(List<String> funcionalidadesExplicadas) {
        this.funcionalidadesExplicadas = funcionalidadesExplicadas;
    }

    public List<PasoGuiaDto> getPasos() {
        return pasos;
    }

    public void setPasos(List<PasoGuiaDto> pasos) {
        this.pasos = pasos;
    }

    public List<String> getPalabrasClave() {
        return palabrasClave;
    }

    public void setPalabrasClave(List<String> palabrasClave) {
        this.palabrasClave = palabrasClave;
    }

    public String getRutaSistema() {
        return rutaSistema;
    }

    public void setRutaSistema(String rutaSistema) {
        this.rutaSistema = rutaSistema;
    }
}
