package com.odin.odin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "ccd_subseries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subseries
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_subserie;

    @NotNull(message = "La serie es obligatorio")
    @Column(name = "id_serie", nullable = false)
    private Long id_serie;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_serie", referencedColumnName = "id_serie", insertable = false, updatable = false)
    private Series serie;

    @NotBlank(message = "El codigo de la subserie es obligatorio")
    @Column(name = "codigo_subserie", nullable = false, unique = true, length = 60)
    private String codigo_subserie;

    @NotBlank(message = "El nombre de la subserie es obligatorio")
    @Column(name = "nombre_subserie", nullable = false, length = 400)
    private String nombre_subserie;

    @Column(name = "tipo_pqrsf", columnDefinition = "text")
    private String tipo_pqrsf;

    @Builder.Default
    @Column(name = "retencion_anios")
    private Integer retencion_anios = 5;

    @Builder.Default
    @Column(name = "disposicion_final", nullable = false, length = 20)
    private String disposicion_final = "CONSERVACION";

    @Builder.Default
    @Column(name = "nivel_acceso", nullable = false, length = 20)
    private String nivel_acceso = "PUBLICO";

    @Builder.Default
    @Column(name = "valor_documental", nullable = false, length = 20)
    private String valor_documental = "ADMINISTRATIVO";

    @Column(name = "observaciones_retencion", columnDefinition = "text")
    private String observaciones_retencion;
    public Long getId_subserie() { return id_subserie; }
    public void setId_subserie(Long id_subserie) { this.id_subserie = id_subserie; }
    // GENERATED_EXPLICIT_ACCESSORS
    public Long getId_serie() { return id_serie; }
    public void setId_serie(Long value) { this.id_serie = value; }
    public Series getSerie() { return serie; }
    public void setSerie(Series value) { this.serie = value; }
    public String getCodigo_subserie() { return codigo_subserie; }
    public void setCodigo_subserie(String value) { this.codigo_subserie = value; }
    public String getNombre_subserie() { return nombre_subserie; }
    public void setNombre_subserie(String value) { this.nombre_subserie = value; }
    public String getTipo_pqrsf() { return tipo_pqrsf; }
    public void setTipo_pqrsf(String value) { this.tipo_pqrsf = value; }
    public String getObservaciones_retencion() { return observaciones_retencion; }
    public void setObservaciones_retencion(String value) { this.observaciones_retencion = value; }
}
