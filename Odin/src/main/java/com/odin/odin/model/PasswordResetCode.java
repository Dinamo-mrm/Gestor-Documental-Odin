package com.odin.odin.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_codes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PasswordResetCode {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;
    @Column(nullable = false, length = 6)
    private String codigo;
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDateTime fechaExpiracion;
    @Builder.Default @Column(nullable = false)
    private Boolean usado = false;
    public boolean estaExpirado() { return fechaExpiracion == null || LocalDateTime.now().isAfter(fechaExpiracion); }
    public boolean estaUsado() { return Boolean.TRUE.equals(usado); }
    public Long getId() { return id; }
    public void setId(Long value) { this.id = value; }
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long value) { this.idUsuario = value; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String value) { this.codigo = value; }
    public java.time.LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(java.time.LocalDateTime value) { this.fechaCreacion = value; }
    public java.time.LocalDateTime getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(java.time.LocalDateTime value) { this.fechaExpiracion = value; }
    public Boolean getUsado() { return usado; }
    public void setUsado(Boolean value) { this.usado = value; }
}
