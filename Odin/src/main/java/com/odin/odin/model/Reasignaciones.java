package com.odin.odin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reasignaciones")
@Getter
@Setter
@NoArgsConstructor
public class Reasignaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_reasignacion;

    @NotNull(message = "El radicado es obligatorio")
    private Integer id_radicado;

    @NotNull(message = "El usuario anterior es obligatorio")
    private Integer id_usuario_anterior;

    @NotNull(message = "El usuario nuevo es obligatorio")
    private Integer id_usuario_nuevo;

    @NotNull(message = "La dependencia nueva es obligatoria")
    private Integer id_dependencia_nueva;

    @NotBlank(message = "La fecha es obligatoria")
    private String fecha;
}
