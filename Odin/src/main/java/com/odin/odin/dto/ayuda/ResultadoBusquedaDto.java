package com.odin.odin.dto.ayuda;

import java.io.Serializable;

/**
 * Representa un resultado consolidado de búsqueda en el Centro de Ayuda.
 */
public class ResultadoBusquedaDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String tipo; // "Tutorial", "Procedimiento", "Pregunta Frecuente"
    private String titulo;
    private String modulo;
    private String snippet;
    private String url;
    private String colorBadge;

    public ResultadoBusquedaDto() {
    }

    public ResultadoBusquedaDto(String tipo, String titulo, String modulo, String snippet, String url, String colorBadge) {
        this.tipo = tipo;
        this.titulo = titulo;
        this.modulo = modulo;
        this.snippet = snippet;
        this.url = url;
        this.colorBadge = colorBadge;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getColorBadge() {
        return colorBadge;
    }

    public void setColorBadge(String colorBadge) {
        this.colorBadge = colorBadge;
    }
}
