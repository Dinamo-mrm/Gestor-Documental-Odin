package com.odin.odin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "documentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Documentos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_documento;

    @NotNull(message = "El id_radicado es obligatorio")
    private Long id_radicado;

    @NotNull(message = "La tamano es obligatoria")
    private Integer tamano;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El nombre_archivo de identificacion")
    private String nombre_archivo;

    @NotBlank(message = "El ruta_archivo es obligatorio")
    private String ruta_archivo;

    @NotBlank(message = "El tipo es obligatorio")
    private String tipo;

    @NotBlank(message = "La fecha_subida es obligatoria")
    private String fecha_subida;

    @Column(name = "mime_type")
    private String mime_type;

    @Column(name = "checksum")
    private String checksum;

    @Column(name = "version_actual")
    private Integer version_actual;

    @Column(name = "fecha_limite_anexo")
    private java.time.OffsetDateTime fecha_limite_anexo;
}