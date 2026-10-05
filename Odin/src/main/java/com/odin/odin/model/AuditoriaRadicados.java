package com.odin.odin.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria_radicados")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuditoriaRadicados {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auditoria")
    private Long id_auditoria;

    @Column(name = "id_radicado", nullable = false)
    private Long id_radicado;
    @Column(name = "id_usuario", nullable = false)
    private Long id_usuario;
    @Column(name = "accion", nullable = false)
    private String accion;
    @Column(name = "campo_modificado")
    private String campo_modificado;
    @Column(name = "valor_anterior", columnDefinition = "text")
    private String valor_anterior;
    @Column(name = "valor_nuevo", columnDefinition = "text")
    private String valor_nuevo;
    @Column(name = "ip")
    private String ip;
    @Column(name = "fecha")
    private LocalDateTime fecha;
    @Column(name = "comentario", columnDefinition = "text")
    private String comentario;
    @Column(name = "tabla_afectada")
    private String tabla_afectada;
    @Column(name = "registro_afectado")
    private Long registro_afectado;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "datos_anteriores", columnDefinition = "jsonb")
    private JsonNode datos_anteriores;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "datos_nuevos", columnDefinition = "jsonb")
    private JsonNode datos_nuevos;
}
