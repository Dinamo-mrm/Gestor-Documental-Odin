package com.odin.odin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "subseries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subseries
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_subserie;

    @NotNull(message = "La serie es obligatorio")
    private Integer id_serie;

    @NotBlank(message = "La subserie es obligatorio")
    private String subserie;

    @NotBlank(message = "La descripcion es obligatoria")
    private String descripcion;

    @NotBlank(message = "El estado es obligatoria")
    private String estado;
}
