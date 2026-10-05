package com.odin.odin.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="log_accesos") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LogAccesos {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_log") private Long idLog;
 @Column(name="id_usuario") private Long idUsuario;
 @Column(name="accion",nullable=false) private String accion;
 @Column(name="ip") private String ip;
 @Column(name="user_agent") private String userAgent;
 @Column(name="exito",nullable=false) private Boolean exito;
 @Column(name="motivo") private String motivo;
 @Column(name="fecha") private LocalDateTime fecha;
}
