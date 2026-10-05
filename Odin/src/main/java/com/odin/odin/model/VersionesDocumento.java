package com.odin.odin.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "versiones_documento",
       uniqueConstraints = @UniqueConstraint(name = "uq_version_documento_numero", columnNames = {"id_documento", "version_numero"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class VersionesDocumento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_version")
    private Long id_version;

    @Column(name = "id_documento", nullable = false)
    private Long id_documento;

    @Column(name = "version_numero", nullable = false)
    private Integer version_numero;

    @Column(name = "ruta_archivo")
    private String ruta_archivo;

    @Column(name = "observacion")
    private String observacion;

    @Column(name = "fecha_version")
    private LocalDateTime fecha_version;

    @Column(name = "creado_por")
    private Long creado_por;
}
