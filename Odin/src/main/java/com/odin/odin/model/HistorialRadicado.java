package com.odin.odin.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_radicado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialRadicado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Long id_historial;

    @Column(name = "id_radicado", nullable = false)
    private Long id_radicado;

    @Column(name = "id_usuario", nullable = false)
    private Long id_usuario;

    @Column(name = "accion", nullable = false, length = 100)
    private String accion;

    @Column(name = "descripcion", nullable = false, columnDefinition = "text")
    private String descripcion;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;
    // GENERATED_EXPLICIT_ACCESSORS
    public Long getId_historial() { return id_historial; }
    public void setId_historial(Long value) { this.id_historial = value; }
    public Long getId_radicado() { return id_radicado; }
    public void setId_radicado(Long value) { this.id_radicado = value; }
    public Long getId_usuario() { return id_usuario; }
    public void setId_usuario(Long value) { this.id_usuario = value; }
    public String getAccion() { return accion; }
    public void setAccion(String value) { this.accion = value; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String value) { this.descripcion = value; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime value) { this.fecha = value; }
}
