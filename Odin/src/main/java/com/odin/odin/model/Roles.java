package com.odin.odin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long id_rol;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "nombre")
    private String nombre;

    @NotBlank(message = "El estado es obligatorio")
    @Column(name = "estado")
    private String estado;

    @NotBlank(message = "El rol es obligatorio")
    @Column(name = "rol")
    private String rol;
    public Long getId_rol() { return id_rol; }
    public void setId_rol(Long value) { this.id_rol = value; }
    public String getNombre() { return nombre; }
    public void setNombre(String value) { this.nombre = value; }
    public String getEstado() { return estado; }
    public void setEstado(String value) { this.estado = value; }
    public String getRol() { return rol; }
    public void setRol(String value) { this.rol = value; }
}
