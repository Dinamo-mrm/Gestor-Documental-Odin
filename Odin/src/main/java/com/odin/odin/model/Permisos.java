package com.odin.odin.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permisos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permisos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_permiso")
    private Long id_permiso;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "modulo", nullable = false)
    private String modulo;

    @Column(name = "accion", nullable = false)
    private String accion;
    // GENERATED_EXPLICIT_ACCESSORS
    public Long getId_permiso() { return id_permiso; }
    public void setId_permiso(Long value) { this.id_permiso = value; }
    public String getNombre() { return nombre; }
    public void setNombre(String value) { this.nombre = value; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String value) { this.descripcion = value; }
    public String getModulo() { return modulo; }
    public void setModulo(String value) { this.modulo = value; }
    public String getAccion() { return accion; }
    public void setAccion(String value) { this.accion = value; }
}
