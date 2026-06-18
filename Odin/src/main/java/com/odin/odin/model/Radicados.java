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
    private Integer id_tramite ;

    @NotNull(message = "el estado es obligatorio")
    private Integer id_estado ;

    @ManyToOne
    @JoinColumn(name = "id_dependencia")
    private Dependencias dependencias; // Asumiendo que tienes una entidad llamada Estado
/*
    @NotNull(message = "la dependencia es obligatoria")
    private String id_dependencia ;

 */

    @NotNull(message = "el usuario es obligatorio")
    private Integer id_usuario ;

    // === CLASIFICACION DOCUMENTAL ===
    // Columnas REALES de la tabla radicados: FK hacia ccd_series(codigo_serie) y ccd_subseries(codigo_subserie).
    @Column(name = "codigo_serie", length = 50)
    private String codigo_serie;

    @Column(name = "codigo_subserie", length = 60)
    private String codigo_subserie;

    // Relaciones de solo lectura para mostrar el nombre/codigo de la serie/subserie en el resumen.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "codigo_serie", referencedColumnName = "codigo_serie", insertable = false, updatable = false)
    private Series serie;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "codigo_subserie", referencedColumnName = "codigo_subserie", insertable = false, updatable = false)
    private Subseries subserie;

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

    // === CAMPOS ADICIONALES DEL FORMULARIO DOCUMENTAL (no persistidos aun) ===
    @Transient
    private String tipoRadicacion;

    @Transient
    private String correo;

    @Transient
    private String telefono;

    @Transient
    private String ciudad;

    @Transient
    private String numeroFolios;

    @Transient
    private String soporte;

    @Transient
    private String etiquetas;

    @Transient
    private String confidencialidad;

    @Transient
    private String fechaLimite;

    @Transient
    private String descripcion;

    @Transient
    private String dependenciaResponsable;

    @Transient
    private String tipoDocumental;

}
