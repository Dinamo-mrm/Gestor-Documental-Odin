package com.odin.odin.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name="notificaciones")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Notificaciones {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_notificacion") private Long id_notificacion;
 @Column(name="id_usuario", nullable=false) private Long id_usuario;
 @Column(name="id_radicado") private Long id_radicado;
 @Column(name="titulo", nullable=false) private String titulo;
 @Column(name="mensaje", nullable=false) private String mensaje;
 @Column(name="leida") private Boolean leida;
 @Column(name="fecha", nullable=false) private LocalDateTime fecha;
}
