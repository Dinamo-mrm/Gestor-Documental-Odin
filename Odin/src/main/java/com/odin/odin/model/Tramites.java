package com.odin.odin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

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
    @Column(
            name = "nombre",
            nullable = false,
            length = 200
    )
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    @Column(
            name = "descripcion",
            nullable = false,
            columnDefinition = "text"
    )
    private String descripcion;

    @Column(name = "id_dependencia_responsable")
    private Long idDependenciaResponsable;

    @Column(name = "id_estado_inicial")
    private Integer idEstadoInicial;

    @Column(name = "dias_respuesta")
    private Integer diasRespuesta;

    @Column(
            name = "prioridad_default",
            length = 20
    )
    private String prioridadDefault;

    @Column(name = "requiere_respuesta")
    private Boolean requiereRespuesta;

    @Builder.Default
    @Column(name = "activo")
    private Boolean activo = true;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "fecha_limite")
    private LocalDateTime fechaLimite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_dependencia_responsable",
            insertable = false,
            updatable = false
    )
    private Dependencias dependenciaResponsable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_estado_inicial",
            insertable = false,
            updatable = false
    )
    private Estados estadoInicial;

    public Integer getIdEstadoInicial() {
        return idEstadoInicial;
    }

    public void setIdEstadoInicial(Integer idEstadoInicial) {
        this.idEstadoInicial = idEstadoInicial;
    }

    public Long getIdDependenciaResponsable() {
        return idDependenciaResponsable;
    }

    public void setIdDependenciaResponsable(
            Long idDependenciaResponsable) {

        this.idDependenciaResponsable =
                idDependenciaResponsable;
    }
    public Long getIdTramite() { return idTramite; }
    public void setIdTramite(Long idTramite) { this.idTramite = idTramite; }
    // GENERATED_EXPLICIT_ACCESSORS
    public String getNombre() { return nombre; }
    public void setNombre(String value) { this.nombre = value; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String value) { this.descripcion = value; }
    public Integer getDiasRespuesta() { return diasRespuesta; }
    public void setDiasRespuesta(Integer value) { this.diasRespuesta = value; }
    public String getPrioridadDefault() { return prioridadDefault; }
    public void setPrioridadDefault(String value) { this.prioridadDefault = value; }
    public Boolean getRequiereRespuesta() { return requiereRespuesta; }
    public void setRequiereRespuesta(Boolean value) { this.requiereRespuesta = value; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime value) { this.fechaCreacion = value; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime value) { this.fechaActualizacion = value; }
    public LocalDateTime getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(LocalDateTime value) { this.fechaLimite = value; }
    public Dependencias getDependenciaResponsable() { return dependenciaResponsable; }
    public void setDependenciaResponsable(Dependencias value) { this.dependenciaResponsable = value; }
    public Estados getEstadoInicial() { return estadoInicial; }
    public void setEstadoInicial(Estados value) { this.estadoInicial = value; }
}
