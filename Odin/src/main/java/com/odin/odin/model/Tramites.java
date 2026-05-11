package com.odin.odin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "tramites")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tramites
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_tramite;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El tipo de identificacion")
    private String descripcion;

}
