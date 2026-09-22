package com.odin.odin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "reasignaciones")
@Getter
@Setter
@NoArgsConstructor
public class Reasignaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reasignacion")
    private Long id_reasignacion;

    @NotNull(message = "El radicado es obligatorio")
    @Column(name = "id_radicado")
    private Integer id_radicado;

    @Column(name = "id_usuario_anterior")
    private Integer id_usuario_anterior;

    @NotNull(message = "El usuario nuevo es obligatorio")
    @Column(name = "id_usuario_nuevo")
    private Integer id_usuario_nuevo;

    @NotNull(message = "La dependencia nueva es obligatoria")
    @Column(name = "id_dependencia_nueva")
    private Integer id_dependencia_nueva;

    @NotNull(message = "La fecha es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @PrePersist
    public void prePersist() {
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }
    }
}