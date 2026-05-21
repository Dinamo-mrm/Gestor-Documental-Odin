package com.odin.odin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "series")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Series
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_serie;

    @NotBlank(message = "La serie es obligatorio")
    private String serie;

    @NotBlank(message = "La descripcion es obligatoria")
    private String descripcion;

    @NotBlank(message = "El estado es obligatoria")
    private String estado;
}
