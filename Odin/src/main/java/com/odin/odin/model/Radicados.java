package com.odin.odin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

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
    private Long id_tramite;

    @NotNull(message = "el estado es obligatorio")
    @Column(name = "id_estado")
    private Integer id_estado;

    @Column(
            name = "id_dependencia",
            insertable = false,
            updatable = false
    )
    private Long id_dependencia;

    @ManyToOne
    @JoinColumn(name = "id_dependencia")
    private Dependencias dependencias;

    @NotNull(message = "el usuario es obligatorio")
    @Column(name = "id_usuario")
    private Long id_usuario;

    @Column(name = "codigo_serie", length = 50)
    private String codigo_serie;

    @Column(name = "id_serie")
    private Long id_serie;

    @Column(name = "codigo_subserie", length = 60)
    private String codigo_subserie;

    @Column(name = "id_subserie")
    private Long id_subserie;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "codigo_serie",
            referencedColumnName = "codigo_serie",
            insertable = false,
            updatable = false
    )
    private Series serie;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "codigo_subserie",
            referencedColumnName = "codigo_subserie",
            insertable = false,
            updatable = false
    )
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

    @Column(name = "fecha_vencimiento")
    private String fecha_vencimiento;

    @Column(name = "fecha_limite")
    private String fecha_limite;

    @Column(name = "tipo_radicado")
    private String tipo_radicado;

    @Column(name = "medio_recepcion")
    private String medio_recepcion;

    @Column(name = "prioridad")
    private String prioridad;

    @Column(name = "confidencialidad")
    private String confidencialidad;

    @Column(name = "fecha_cierre")
    private LocalDateTime fecha_cierre;

    @Column(name = "id_usuario_cierre")
    private Long id_usuario_cierre;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "id_tramite",
            referencedColumnName = "id_tramite",
            insertable = false,
            updatable = false
    )
    private Tramites tramite;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "id_usuario",
            insertable = false,
            updatable = false
    )
    private Usuarios usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "id_estado",
            referencedColumnName = "id_estado",
            insertable = false,
            updatable = false
    )
    private Estados estado;

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
    private String fechaLimite;

    @Transient
    private String descripcion;

    @Transient
    private String dependenciaResponsable;

    @Transient
    private String tipoDocumental;
    // GENERATED_EXPLICIT_ACCESSORS
    public Long getId_radicado() { return id_radicado; }
    public void setId_radicado(Long value) { this.id_radicado = value; }
    public String getNumero_radicado() { return numero_radicado; }
    public void setNumero_radicado(String value) { this.numero_radicado = value; }
    public Long getId_tramite() { return id_tramite; }
    public void setId_tramite(Long value) { this.id_tramite = value; }
    public Integer getId_estado() { return id_estado; }
    public void setId_estado(Integer value) { this.id_estado = value; }
    public Long getId_dependencia() { return id_dependencia; }
    public void setId_dependencia(Long value) { this.id_dependencia = value; }
    public Dependencias getDependencias() { return dependencias; }
    public void setDependencias(Dependencias value) { this.dependencias = value; }
    public Long getId_usuario() { return id_usuario; }
    public void setId_usuario(Long value) { this.id_usuario = value; }
    public String getCodigo_serie() { return codigo_serie; }
    public void setCodigo_serie(String value) { this.codigo_serie = value; }
    public Long getId_serie() { return id_serie; }
    public void setId_serie(Long value) { this.id_serie = value; }
    public String getCodigo_subserie() { return codigo_subserie; }
    public void setCodigo_subserie(String value) { this.codigo_subserie = value; }
    public Long getId_subserie() { return id_subserie; }
    public void setId_subserie(Long value) { this.id_subserie = value; }
    public Series getSerie() { return serie; }
    public void setSerie(Series value) { this.serie = value; }
    public Subseries getSubserie() { return subserie; }
    public void setSubserie(Subseries value) { this.subserie = value; }
    public String getRemitente() { return remitente; }
    public void setRemitente(String value) { this.remitente = value; }
    public String getAsunto() { return asunto; }
    public void setAsunto(String value) { this.asunto = value; }
    public String getFecha_radicado() { return fecha_radicado; }
    public void setFecha_radicado(String value) { this.fecha_radicado = value; }
    public String getFecha_vencimiento() { return fecha_vencimiento; }
    public void setFecha_vencimiento(String value) { this.fecha_vencimiento = value; }
    public String getFecha_limite() { return fecha_limite; }
    public void setFecha_limite(String value) { this.fecha_limite = value; }
    public String getTipo_radicado() { return tipo_radicado; }
    public void setTipo_radicado(String value) { this.tipo_radicado = value; }
    public String getMedio_recepcion() { return medio_recepcion; }
    public void setMedio_recepcion(String value) { this.medio_recepcion = value; }
    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String value) { this.prioridad = value; }
    public String getConfidencialidad() { return confidencialidad; }
    public void setConfidencialidad(String value) { this.confidencialidad = value; }
    public LocalDateTime getFecha_cierre() { return fecha_cierre; }
    public void setFecha_cierre(LocalDateTime value) { this.fecha_cierre = value; }
    public Long getId_usuario_cierre() { return id_usuario_cierre; }
    public void setId_usuario_cierre(Long value) { this.id_usuario_cierre = value; }
    public Tramites getTramite() { return tramite; }
    public void setTramite(Tramites value) { this.tramite = value; }
    public Usuarios getUsuario() { return usuario; }
    public void setUsuario(Usuarios value) { this.usuario = value; }
    public Estados getEstado() { return estado; }
    public void setEstado(Estados value) { this.estado = value; }
    public String getNumeroIdentificacion() { return numeroIdentificacion; }
    public void setNumeroIdentificacion(String value) { this.numeroIdentificacion = value; }
    public String getContacto() { return contacto; }
    public void setContacto(String value) { this.contacto = value; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String value) { this.direccion = value; }
    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String value) { this.tipoDocumento = value; }
    public String getFechaDocumento() { return fechaDocumento; }
    public void setFechaDocumento(String value) { this.fechaDocumento = value; }
    public String getCanalRecepcion() { return canalRecepcion; }
    public void setCanalRecepcion(String value) { this.canalRecepcion = value; }
    public String getDependencia() { return dependencia; }
    public void setDependencia(String value) { this.dependencia = value; }
    public MultipartFile[] getArchivos() { return archivos; }
    public void setArchivos(MultipartFile[] value) { this.archivos = value; }
    public String getDependenciaOrigen() { return dependenciaOrigen; }
    public void setDependenciaOrigen(String value) { this.dependenciaOrigen = value; }
    public String getDependenciaDestino() { return dependenciaDestino; }
    public void setDependenciaDestino(String value) { this.dependenciaDestino = value; }
    public String getResponsable() { return responsable; }
    public void setResponsable(String value) { this.responsable = value; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String value) { this.observaciones = value; }
    public String getTipoPQRS() { return tipoPQRS; }
    public void setTipoPQRS(String value) { this.tipoPQRS = value; }
    public String getTipoRadicacion() { return tipoRadicacion; }
    public void setTipoRadicacion(String value) { this.tipoRadicacion = value; }
    public String getCorreo() { return correo; }
    public void setCorreo(String value) { this.correo = value; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String value) { this.telefono = value; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String value) { this.ciudad = value; }
    public String getNumeroFolios() { return numeroFolios; }
    public void setNumeroFolios(String value) { this.numeroFolios = value; }
    public String getSoporte() { return soporte; }
    public void setSoporte(String value) { this.soporte = value; }
    public String getEtiquetas() { return etiquetas; }
    public void setEtiquetas(String value) { this.etiquetas = value; }
    public String getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(String value) { this.fechaLimite = value; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String value) { this.descripcion = value; }
    public String getDependenciaResponsable() { return dependenciaResponsable; }
    public void setDependenciaResponsable(String value) { this.dependenciaResponsable = value; }
    public String getTipoDocumental() { return tipoDocumental; }
    public void setTipoDocumental(String value) { this.tipoDocumental = value; }
}
