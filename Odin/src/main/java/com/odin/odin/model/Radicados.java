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
public class Radicados {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_radicado")
    private Long id_radicado;

    @NotNull(message = "el numero de radicado es obligatorio")
    @Column(name = "numero_radicado")
    private String numero_radicado;

    @NotNull(message = "el tramite es obligatorio")
    @Column(name = "id_tramite")
    private Integer id_tramite;

    @NotNull(message = "el estado es obligatorio")
    @Column(name = "id_estado")
    private Integer id_estado;

    // ✅ AGREGAR ESTE CAMPO
    @Column(name = "id_dependencia", insertable = false, updatable = false)
    private Long id_dependencia;

    @ManyToOne
    @JoinColumn(name = "id_dependencia")
    private Dependencias dependencias;

    @NotNull(message = "el usuario es obligatorio")
    @Column(name = "id_usuario")
    private Integer id_usuario;

    // === CLASIFICACION DOCUMENTAL ===
    @Column(name = "codigo_serie", length = 50)
    private String codigo_serie;

    @Column(name = "codigo_subserie", length = 60)
    private String codigo_subserie;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "codigo_serie", referencedColumnName = "codigo_serie", insertable = false, updatable = false)
    private Series serie;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "codigo_subserie", referencedColumnName = "codigo_subserie", insertable = false, updatable = false)
    private Subseries subserie;

    @NotBlank(message = "el remitente es obligatorio")
    @Column(name = "remitente")
    private String remitente;

    @NotBlank(message = "el asunto es obligatorio")
    @Column(name = "asunto")
    private String asunto;

    @NotBlank(message = "la fecha de radicado es obligatoria")
    @Column(name = "fecha_radicado")
    private String fecha_radicado;

    // ✅ AGREGAR ESTE CAMPO PARA FECHA VENCIMIENTO
    @Column(name = "fecha_vencimiento")
    private String fecha_vencimiento;

    // ✅ AGREGAR ESTE CAMPO PARA FECHA LIMITE
    @Column(name = "fecha_limite")
    private String fecha_limite;

    // ============================================================
    // CAMPOS TRANSIENT (no persisten en BD)
    // ============================================================
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