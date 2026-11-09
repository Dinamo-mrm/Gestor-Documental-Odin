package com.odin.odin.dto.ayuda;

import java.io.Serializable;

/**
 * Representa un paso específico dentro de una guía tutorial o procedimiento de ODIN.
 */
public class PasoGuiaDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private int numero;
    private String titulo;
    private String instruccion;
    private String nota;
    private String resultadoEsperado;

    public PasoGuiaDto() {
    }

    public PasoGuiaDto(int numero, String titulo, String instruccion, String nota, String resultadoEsperado) {
        this.numero = numero;
        this.titulo = titulo;
        this.instruccion = instruccion;
        this.nota = nota;
        this.resultadoEsperado = resultadoEsperado;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getInstruccion() {
        return instruccion;
    }

    public void setInstruccion(String instruccion) {
        this.instruccion = instruccion;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getResultadoEsperado() {
        return resultadoEsperado;
    }

    public void setResultadoEsperado(String resultadoEsperado) {
        this.resultadoEsperado = resultadoEsperado;
    }
}
