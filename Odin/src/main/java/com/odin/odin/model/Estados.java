package com.odin.odin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "estados")
@Getter
@Setter
@NoArgsConstructor
public class Estados {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Integer id_estado;

    @NotBlank(message = "El nombre del estado es obligatorio")
    @Column(name = "nombre", nullable = false)
    private String nombre;
    // GENERATED_EXPLICIT_ACCESSORS
    public Integer getId_estado() { return id_estado; }
    public void setId_estado(Integer value) { this.id_estado = value; }
    public String getNombre() { return nombre; }
    public void setNombre(String value) { this.nombre = value; }
}
