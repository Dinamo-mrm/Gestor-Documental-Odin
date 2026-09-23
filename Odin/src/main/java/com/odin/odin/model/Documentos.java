package com.odin.odin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "documentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Documentos
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_documento;

    @NotNull(message = "El id_radicado es obligatorio")
    private Long id_radicado;

    @NotNull(message = "La tamano es obligatoria")
    private Integer tamano;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El nombre_archivo de identificacion")
    private String nombre_archivo;

    @NotBlank(message = "El ruta_archivo es obligatorio")
    private String ruta_archivo;

    @NotBlank(message = "El tipo es obligatorio")
    private String tipo;

    @NotBlank(message = "La fecha_subida es obligatoria")
    private String fecha_subida;
    // GENERATED_EXPLICIT_ACCESSORS
    public Long getId_documento() { return id_documento; }
    public void setId_documento(Long value) { this.id_documento = value; }
    public Long getId_radicado() { return id_radicado; }
    public void setId_radicado(Long value) { this.id_radicado = value; }
    public Integer getTamano() { return tamano; }
    public void setTamano(Integer value) { this.tamano = value; }
    public String getNombre() { return nombre; }
    public void setNombre(String value) { this.nombre = value; }
    public String getNombre_archivo() { return nombre_archivo; }
    public void setNombre_archivo(String value) { this.nombre_archivo = value; }
    public String getRuta_archivo() { return ruta_archivo; }
    public void setRuta_archivo(String value) { this.ruta_archivo = value; }
    public String getTipo() { return tipo; }
    public void setTipo(String value) { this.tipo = value; }
    public String getFecha_subida() { return fecha_subida; }
    public void setFecha_subida(String value) { this.fecha_subida = value; }
}
