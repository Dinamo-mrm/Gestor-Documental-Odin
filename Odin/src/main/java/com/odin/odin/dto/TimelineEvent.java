package com.odin.odin.dto;

import java.time.LocalDateTime;

/**
 * Evento unificado de la línea temporal de un radicado
 * (historial, observación, documento, firma, reasignación, anexo).
 */
public class TimelineEvent implements Comparable<TimelineEvent> {

    private LocalDateTime fecha;
    private String fechaTexto;
    private String tipo;       // HISTORIAL | OBSERVACION | DOCUMENTO | FIRMA | REASIGNACION | ANEXO | AUDITORIA
    private String titulo;
    private String descripcion;
    private String usuario;
    private String meta;       // estado, dependencia, archivo, etc.
    private String icono;      // hint CSS class

    public TimelineEvent() {}

    public TimelineEvent(LocalDateTime fecha, String fechaTexto, String tipo, String titulo,
                         String descripcion, String usuario, String meta, String icono) {
        this.fecha = fecha;
        this.fechaTexto = fechaTexto;
        this.tipo = tipo;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.usuario = usuario;
        this.meta = meta;
        this.icono = icono;
    }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public String getFechaTexto() { return fechaTexto; }
    public void setFechaTexto(String fechaTexto) { this.fechaTexto = fechaTexto; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getMeta() { return meta; }
    public void setMeta(String meta) { this.meta = meta; }
    public String getIcono() { return icono; }
    public void setIcono(String icono) { this.icono = icono; }

    @Override
    public int compareTo(TimelineEvent o) {
        if (this.fecha == null && o.fecha == null) return 0;
        if (this.fecha == null) return 1;
        if (o.fecha == null) return -1;
        return o.fecha.compareTo(this.fecha); // más reciente primero
    }
}
