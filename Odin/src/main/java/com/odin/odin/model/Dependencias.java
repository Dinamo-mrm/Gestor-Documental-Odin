package com.odin.odin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
//nombre de la tabla con la que queremos hacer la conexion
@Table(name = "dependencias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dependencias

{
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id_dependencia;

        @NotBlank(message = "el rol es obligatorio")
        private String dependencia;

        @NotBlank(message = "el estado es obligatorio")
        private String estado;

        @NotBlank(message = "la descripcion es obligatorio")
        private String descripcion;

        @NotBlank(message = "el nombre es obligatorio")
        private String nombre;
    // GENERATED_EXPLICIT_ACCESSORS
    public Long getId_dependencia() { return id_dependencia; }
    public void setId_dependencia(Long value) { this.id_dependencia = value; }
    public String getDependencia() { return dependencia; }
    public void setDependencia(String value) { this.dependencia = value; }
    public String getEstado() { return estado; }
    public void setEstado(String value) { this.estado = value; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String value) { this.descripcion = value; }
    public String getNombre() { return nombre; }
    public void setNombre(String value) { this.nombre = value; }
}
