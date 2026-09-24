package com.odin.odin.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ccd_unidades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CcdUnidad
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_unidad;

    @Column(name = "codigo_unidad", nullable = false, unique = true, length = 30)
    private String codigo_unidad;

    @Column(name = "nombre_unidad", nullable = false, length = 255)
    private String nombre_unidad;

    @Column(name = "codigo_padre", length = 30)
    private String codigo_padre;

    @Column(name = "nivel", nullable = false)
    private Integer nivel;

    @Column(name = "descripcion", columnDefinition = "text")
    private String descripcion;
    // GENERATED_EXPLICIT_ACCESSORS
    public Long getId_unidad() { return id_unidad; }
    public void setId_unidad(Long value) { this.id_unidad = value; }
    public String getCodigo_unidad() { return codigo_unidad; }
    public void setCodigo_unidad(String value) { this.codigo_unidad = value; }
    public String getNombre_unidad() { return nombre_unidad; }
    public void setNombre_unidad(String value) { this.nombre_unidad = value; }
    public String getCodigo_padre() { return codigo_padre; }
    public void setCodigo_padre(String value) { this.codigo_padre = value; }
    public Integer getNivel() { return nivel; }
    public void setNivel(Integer value) { this.nivel = value; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String value) { this.descripcion = value; }
}
