package com.odin.odin.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "observaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Observaciones {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_observacion")
    private Long id_observacion;

    @Column(name = "id_radicado")
    private Long id_radicado;

    @Column(name = "id_usuario")
    private Long id_usuario;

    @Column(name = "comentario", nullable = false, length = 255)
    private String comentario;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;
    // GENERATED_EXPLICIT_ACCESSORS
    public Long getId_observacion() { return id_observacion; }
    public void setId_observacion(Long value) { this.id_observacion = value; }
    public Long getId_radicado() { return id_radicado; }
    public void setId_radicado(Long value) { this.id_radicado = value; }
    public Long getId_usuario() { return id_usuario; }
    public void setId_usuario(Long value) { this.id_usuario = value; }
    public String getComentario() { return comentario; }
    public void setComentario(String value) { this.comentario = value; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime value) { this.fecha = value; }
}
