package com.odin.odin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "remitentes_externos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RemitentesExternos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_remitente")
    private Long id_remitente;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Column(name = "apellido", nullable = false)
    private String apellido;

    @NotBlank(message = "El tipo de identificación es obligatorio")
    @Column(name = "tipo_identificacion", nullable = false)
    private String tipo_identificacion;

    @NotBlank(message = "El número de identificación es obligatorio")
    @Column(name = "num_identificacion", nullable = false, unique = true)
    private String num_identificacion;

    @Column(name = "correo")
    private String correo;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "fecha_registro")
    private String fecha_registro;
    // GENERATED_EXPLICIT_ACCESSORS
    public Long getId_remitente() { return id_remitente; }
    public void setId_remitente(Long value) { this.id_remitente = value; }
    public String getNombre() { return nombre; }
    public void setNombre(String value) { this.nombre = value; }
    public String getApellido() { return apellido; }
    public void setApellido(String value) { this.apellido = value; }
    public String getTipo_identificacion() { return tipo_identificacion; }
    public void setTipo_identificacion(String value) { this.tipo_identificacion = value; }
    public String getNum_identificacion() { return num_identificacion; }
    public void setNum_identificacion(String value) { this.num_identificacion = value; }
    public String getCorreo() { return correo; }
    public void setCorreo(String value) { this.correo = value; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String value) { this.telefono = value; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String value) { this.direccion = value; }
    public String getFecha_registro() { return fecha_registro; }
    public void setFecha_registro(String value) { this.fecha_registro = value; }
}
