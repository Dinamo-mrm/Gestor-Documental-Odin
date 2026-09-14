package com.odin.odin.model;

import jakarta.persistence.*;
import lombok.*;

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
    private Long id_historial;

    @Column(name = "id_radicado", nullable = false)
    private Long id_radicado;

    @Column(name = "id_usuario")
    private Integer id_usuario;

    @Column(name = "accion", nullable = false, length = 80)
    private String accion;

    @Column(name = "descripcion", nullable = false, length = 500)
    private String descripcion;

    @Column(name = "fecha", nullable = false)
    private String fecha;
}
