package com.odin.odin.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name="sesiones_usuario")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SesionesUsuario {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_sesion") private Long idSesion;
    @Column(name="id_usuario") private Long idUsuario;
    @Column(name="fecha_inicio") private LocalDateTime fechaInicio;
    @Column(name="fecha_fin") private LocalDateTime fechaFin;
    @Column(name="ip") private String ip;
    @Column(name="navegador") private String navegador;
    @Column(name="estado") private String estado;
    @Column(name="token", columnDefinition="text") private String token;
    @Column(name="ultima_actividad") private LocalDateTime ultimaActividad;
    @Column(name="dispositivo", columnDefinition="text") private String dispositivo;
}
