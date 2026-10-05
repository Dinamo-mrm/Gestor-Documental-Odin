package com.odin.odin.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "expedientes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Expedientes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_expediente")
    private Long id_expediente;

    @Column(name = "codigo_expediente", nullable = false, unique = true)
    private String codigo_expediente;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fecha_creacion")
    private LocalDateTime fecha_creacion;

    @Column(name = "estado")
    private String estado;
}
