package com.odin.odin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "ccd_series")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Series
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_serie;

    @NotBlank(message = "El codigo de la serie es obligatorio")
    @Column(name = "codigo_serie", nullable = false, unique = true, length = 50)
    private String codigo_serie;

    @NotBlank(message = "El nombre de la serie es obligatorio")
    @Column(name = "nombre_serie", nullable = false, length = 300)
    private String nombre_serie;

    @NotBlank(message = "El codigo de unidad es obligatorio")
    @Column(name = "codigo_unidad", nullable = false, length = 30)
    private String codigo_unidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "codigo_unidad", referencedColumnName = "codigo_unidad", insertable = false, updatable = false)
    private CcdUnidad unidad;

    @Column(name = "codigo_seccion", length = 30)
    private String codigo_seccion;

    @Column(name = "codigo_subseccion", length = 30)
    private String codigo_subseccion;

    @Column(name = "descripcion", columnDefinition = "text")
    private String descripcion;

    @Column(name = "informacion_publica", length = 50)
    private String informacion_publica;
    public Long getId_serie() { return id_serie; }
    public void setId_serie(Long id_serie) { this.id_serie = id_serie; }
    // GENERATED_EXPLICIT_ACCESSORS
    public String getCodigo_serie() { return codigo_serie; }
    public void setCodigo_serie(String value) { this.codigo_serie = value; }
    public String getNombre_serie() { return nombre_serie; }
    public void setNombre_serie(String value) { this.nombre_serie = value; }
    public String getCodigo_unidad() { return codigo_unidad; }
    public void setCodigo_unidad(String value) { this.codigo_unidad = value; }
    public CcdUnidad getUnidad() { return unidad; }
    public void setUnidad(CcdUnidad value) { this.unidad = value; }
    public String getCodigo_seccion() { return codigo_seccion; }
    public void setCodigo_seccion(String value) { this.codigo_seccion = value; }
    public String getCodigo_subseccion() { return codigo_subseccion; }
    public void setCodigo_subseccion(String value) { this.codigo_subseccion = value; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String value) { this.descripcion = value; }
    public String getInformacion_publica() { return informacion_publica; }
    public void setInformacion_publica(String value) { this.informacion_publica = value; }
}
