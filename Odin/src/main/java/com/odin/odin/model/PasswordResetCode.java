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
}
