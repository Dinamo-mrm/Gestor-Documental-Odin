package com.odin.odin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

import lombok.*;

@Entity
@Table(name = "tramites")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tramites {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tramite")
    private Long idTramite;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @Column(name = "id_dependencia_responsable")
    private Long idDependenciaResponsable;

    @Column(name = "id_estado_inicial")
    private Long idEstadoInicial;

    @Column(name = "dias_respuesta")
    private Integer diasRespuesta;

    @Column(name = "prioridad_default")
    private String prioridadDefault;

    @Column(name = "requiere_respuesta")
    private Boolean requiereRespuesta;

    @Builder.Default
    private Boolean activo = true;

    private LocalDateTime fecha_creacion;

    private LocalDateTime fecha_actualizacion;
}