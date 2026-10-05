package com.odin.odin.model;
import jakarta.persistence.*; import lombok.*; import java.time.LocalDateTime;
@Entity @Table(name="anexos") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Anexos { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_anexo") private Long idAnexo; @Column(name="id_radicado") private Long idRadicado; @Column(name="descripcion",nullable=false) private String descripcion; @Column(name="archivo",nullable=false,columnDefinition="text") private String archivo; @Column(name="fecha_limite") private LocalDateTime fechaLimite; }
