package com.odin.odin.model;

import jakarta.persistence.*;
import lombok.*;

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
    private Long id_observacion;

    @Column(name = "id_radicado", nullable = false)
    private Long id_radicado;

    @Column(name = "id_usuario")
    private Integer id_usuario;

    @Column(name = "comentario", nullable = false, length = 255)
    private String comentario;

    @Column(name = "fecha", nullable = false)
    private String fecha;
}
