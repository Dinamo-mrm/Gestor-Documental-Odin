package com.odin.odin.dto.ayuda;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una pregunta y respuesta frecuente (FAQ) de ODIN.
 */
public class PreguntaFrecuenteDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String categoria;
    private String pregunta;
    private String respuesta;
    private List<String> palabrasClave = new ArrayList<>();
    private String moduloRelacionado;
    private String rutaAcceso;

    public PreguntaFrecuenteDto() {
    }

    public PreguntaFrecuenteDto(String id, String categoria, String pregunta, String respuesta, String moduloRelacionado, String rutaAcceso) {
        this.id = id;
        this.categoria = categoria;
        this.pregunta = pregunta;
        this.respuesta = respuesta;
        this.moduloRelacionado = moduloRelacionado;
        this.rutaAcceso = rutaAcceso;
    }

    public PreguntaFrecuenteDto(String id, String categoria, String pregunta, String respuesta, String moduloRelacionado, String rutaAcceso, List<String> palabrasClave) {
        this.id = id;
        this.categoria = categoria;
        this.pregunta = pregunta;
        this.respuesta = respuesta;
        this.moduloRelacionado = moduloRelacionado;
        this.rutaAcceso = rutaAcceso;
        if (palabrasClave != null) {
            this.palabrasClave = new ArrayList<>(palabrasClave);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public List<String> getPalabrasClave() {
        return palabrasClave;
    }

    public void setPalabrasClave(List<String> palabrasClave) {
        this.palabrasClave = palabrasClave;
    }

    public String getPalabrasClaveTexto() {
        if (palabrasClave == null || palabrasClave.isEmpty()) {
            return "";
        }
        return String.join(" ", palabrasClave);
    }

    public String getModuloRelacionado() {
        return moduloRelacionado;
    }

    public void setModuloRelacionado(String moduloRelacionado) {
        this.moduloRelacionado = moduloRelacionado;
    }

    public String getRutaAcceso() {
        return rutaAcceso;
    }

    public void setRutaAcceso(String rutaAcceso) {
        this.rutaAcceso = rutaAcceso;
    }
}
