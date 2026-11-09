package com.odin.odin.dto.ayuda;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un procedimiento documentado del Manual de Usuario de ODIN.
 */
public class ManualProcedimientoDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String codigo;
    private String modulo;
    private String nombreProcedimiento;
    private String objetivo;
    private List<String> requisitosPrevios = new ArrayList<>();
    private List<PasoGuiaDto> pasos = new ArrayList<>();
    private String resultadoEsperado;
    private String rutaSistema;

    public ManualProcedimientoDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getNombreProcedimiento() {
        return nombreProcedimiento;
    }

    public void setNombreProcedimiento(String nombreProcedimiento) {
        this.nombreProcedimiento = nombreProcedimiento;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public List<String> getRequisitosPrevios() {
        return requisitosPrevios;
    }

    public void setRequisitosPrevios(List<String> requisitosPrevios) {
        this.requisitosPrevios = requisitosPrevios;
    }

    public List<PasoGuiaDto> getPasos() {
        return pasos;
    }

    public void setPasos(List<PasoGuiaDto> pasos) {
        this.pasos = pasos;
    }

    public String getResultadoEsperado() {
        return resultadoEsperado;
    }

    public void setResultadoEsperado(String resultadoEsperado) {
        this.resultadoEsperado = resultadoEsperado;
    }

    public String getRutaSistema() {
        return rutaSistema;
    }

    public void setRutaSistema(String rutaSistema) {
        this.rutaSistema = rutaSistema;
    }
}
