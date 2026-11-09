package com.odin.odin.dto.ayuda;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa el video general introductorio de ODIN.
 */
public class VideoIntroductorioDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String titulo;
    private String descripcion;
    private String duracionEstimada;
    private String videoUrl;
    private String embedVideoUrl;
    private List<String> temasTratados = new ArrayList<>();

    public VideoIntroductorioDto() {
    }

    public boolean isTieneVideo() {
        return embedVideoUrl != null && !embedVideoUrl.trim().isEmpty();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDuracionEstimada() {
        return duracionEstimada;
    }

    public void setDuracionEstimada(String duracionEstimada) {
        this.duracionEstimada = duracionEstimada;
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

    public List<String> getTemasTratados() {
        return temasTratados;
    }

    public void setTemasTratados(List<String> temasTratados) {
        this.temasTratados = temasTratados;
    }
}
