package com.odin.odin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@Entity
@Table(name = "radicados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class Radicados
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_radicado ;

    @NotNull(message = "el numero de radicado es obligatorio")
    private String numero_radicado;

    @NotNull(message = "el tramite es obligatorio")
    private String id_tramite ;

    @NotNull(message = "el estado es obligatorio")
    private String id_estado ;

    @NotNull(message = "la dependencia es obligatoria")
    private String id_dependencia ;

    @NotNull(message = "el usuario es obligatorio")
    private String id_usuario ;

    @NotBlank(message = "el remitente es obligatorio")
    private String remitente ;

    @NotBlank(message = "el asunto es obligatorio")
    private String asunto ;

    @NotBlank(message = "la fecha de radicado es obligatoria")
    private String fecha_radicado ;

    @Transient
    private String numeroIdentificacion;

    @Transient
    private String contacto;

    @Transient
    private String direccion;

    @Transient
    private String tipoDocumento;

    @Transient
    private String fechaDocumento;

    @Transient
    private String canalRecepcion;

    @Transient
    private String dependencia;

    @Transient
    private MultipartFile[] archivos;

    @Transient
    private String dependenciaOrigen;

    @Transient
    private String dependenciaDestino;

    @Transient
    private String responsable;

    @Transient
    private String prioridad;

    @Transient
    private String observaciones;

    @Transient
    private String tipoPQRS;

}
