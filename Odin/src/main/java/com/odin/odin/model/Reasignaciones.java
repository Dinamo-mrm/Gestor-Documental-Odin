package com.odin.odin.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * Alineado 1:1 con public.reasignaciones en Supabase:
 *
 *   id_reasignacion      bigserial PRIMARY KEY
 *   id_radicado          bigint NULL
 *   id_usuario_anterior  bigint NULL
 *   id_usuario_nuevo     bigint NULL
 *   id_dependencia_nueva bigint NULL
 *   fecha                timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP
 */
@Entity
@Table(name = "reasignaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reasignaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reasignacion")
    private Long id_reasignacion;

    @Column(name = "id_radicado")
    private Long id_radicado;

    @Column(name = "id_usuario_anterior")
    private Long id_usuario_anterior;

    @Column(name = "id_usuario_nuevo")
    private Long id_usuario_nuevo;

    @Column(name = "id_dependencia_nueva")
    private Long id_dependencia_nueva;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "fecha", nullable = false, columnDefinition = "timestamp without time zone")
    private LocalDateTime fecha;

    @PrePersist
    protected void onCreate() {
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }
    }
    public Long getId_reasignacion() { return id_reasignacion; }
    public void setId_reasignacion(Long value) { this.id_reasignacion = value; }
    public Integer getId_radicado() { return id_radicado; }
    public void setId_radicado(Integer value) { this.id_radicado = value; }
    public Long getId_usuario_anterior() { return id_usuario_anterior; }
    public void setId_usuario_anterior(Long value) { this.id_usuario_anterior = value; }
    public Long getId_usuario_nuevo() { return id_usuario_nuevo; }
    public void setId_usuario_nuevo(Long value) { this.id_usuario_nuevo = value; }
    public Long getId_dependencia_nueva() { return id_dependencia_nueva; }
    public void setId_dependencia_nueva(Long value) { this.id_dependencia_nueva = value; }
    public java.time.LocalDateTime getFecha() { return fecha; }
    public void setFecha(java.time.LocalDateTime value) { this.fecha = value; }
}
