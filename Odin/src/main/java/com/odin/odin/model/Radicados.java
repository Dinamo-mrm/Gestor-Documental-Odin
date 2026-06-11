package com.odin.odin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    private Long id_radicado ;

    @NotBlank(message = "el numero de radicado es obligatorio")
    @Column(name = "numero_radicado", nullable = false, unique = true, length = 255)
    private String numero_radicado;

    @Column(name = "id_tramite")
    private Long id_tramite ;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tramite", referencedColumnName = "id_tramite", insertable = false, updatable = false)
    private Tramites tramite;

    @Column(name = "id_estado")
    private Long id_estado ;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_estado", referencedColumnName = "id_estado", insertable = false, updatable = false)
    private Estados estado;

    @Column(name = "id_dependencia")
    private Long id_dependencia ;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_dependencia", referencedColumnName = "id_dependencia", insertable = false, updatable = false)
    private Dependencias dependenciaCatalogo;

    @Column(name = "id_usuario")
    private Long id_usuario ;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", insertable = false, updatable = false)
    private Usuarios usuarioCatalogo;

    @Column(name = "id_remitente")
    private Long id_remitente;

    @NotBlank(message = "el remitente es obligatorio")
    @Column(name = "remitente", nullable = false, length = 255)
    private String remitente ;

    @NotBlank(message = "el asunto es obligatorio")
    @Column(name = "asunto", nullable = false, length = 255)
    private String asunto ;

    @Column(name = "fecha_radicado", nullable = false)
    private String fecha_radicado ;

    @Column(name = "codigo_serie", length = 50)
    private String codigo_serie;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "codigo_serie", referencedColumnName = "codigo_serie", insertable = false, updatable = false)
    private Series serie;

    @Column(name = "codigo_subserie", length = 60)
    private String codigo_subserie;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "codigo_subserie", referencedColumnName = "codigo_subserie", insertable = false, updatable = false)
    private Subseries subserie;

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
