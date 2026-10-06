package com.odin.odin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Entity
@Table(name = "plantilla")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class Plantilla
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_evento;

    @NotBlank(message = "El radicado es obligatorio")
    private long id_radicado;

    @NotBlank(message = "el usuario es obligatoria")
    private long id_usuario;

    @NotBlank(message = "la accion es obligatorio")
    private String accion ;

    @NotNull(message = "la descripcion es obligatorio")
    private String descripcion ;

    @NotNull(message = "la fecha es obligatorio")
    private String fecha ;







    // GENERATED_EXPLICIT_ACCESSORS
    public long getId_evento() { return id_evento; }
    public void setId_evento(long value) { this.id_evento = value; }
    public long getId_radicado() { return id_radicado; }
    public void setId_radicado(long value) { this.id_radicado = value; }
    public long getId_usuario() { return id_usuario; }
    public void setId_usuario(long value) { this.id_usuario = value; }
    public String getAccion() { return accion; }
    public void setAccion(String value) { this.accion = value; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String value) { this.descripcion = value; }
    public String getFecha() { return fecha; }
    public void setFecha(String value) { this.fecha = value; }
}
