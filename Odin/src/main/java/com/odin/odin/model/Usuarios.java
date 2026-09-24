package com.odin.odin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id_usuario;

    @NotNull(message = "El rol es obligatorio")
    @Column(name = "id_rol")
    private Long id_rol;

    @NotNull(message = "La dependencia es obligatoria")
    @Column(name = "id_dependencia")
    private Long id_dependencia;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "nombre")
    private String nombre;

    @NotBlank(message = "El tipo de identificación es obligatorio")
    @Column(name = "tipo_identificacion")
    private String tipo_identificacion;

    @NotBlank(message = "El número de identificación es obligatorio")
    @Column(name = "num_identificacion")
    private String num_identificacion;

    @NotBlank(message = "El correo es obligatorio")
    @Column(name = "correo", unique = true)
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Column(name = "password")
    private String password;

    @NotBlank(message = "La dirección es obligatoria")
    @Column(name = "direccion")
    private String direccion;

    @NotBlank(message = "El teléfono es obligatorio")
    @Column(name = "telefono")
    private String telefono;

    @NotBlank(message = "El estado es obligatorio")
    @Column(name = "estado")
    private String estado;

    @Column(name = "fecha_creacion")
    private String fecha_creacion;
    // GENERATED_EXPLICIT_ACCESSORS
    public Long getId_usuario() { return id_usuario; }
    public void setId_usuario(Long value) { this.id_usuario = value; }
    public Long getId_rol() { return id_rol; }
    public void setId_rol(Long value) { this.id_rol = value; }
    public Long getId_dependencia() { return id_dependencia; }
    public void setId_dependencia(Long value) { this.id_dependencia = value; }
    public String getNombre() { return nombre; }
    public void setNombre(String value) { this.nombre = value; }
    public String getTipo_identificacion() { return tipo_identificacion; }
    public void setTipo_identificacion(String value) { this.tipo_identificacion = value; }
    public String getNum_identificacion() { return num_identificacion; }
    public void setNum_identificacion(String value) { this.num_identificacion = value; }
    public String getCorreo() { return correo; }
    public void setCorreo(String value) { this.correo = value; }
    public String getPassword() { return password; }
    public void setPassword(String value) { this.password = value; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String value) { this.direccion = value; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String value) { this.telefono = value; }
    public String getEstado() { return estado; }
    public void setEstado(String value) { this.estado = value; }
    public String getFecha_creacion() { return fecha_creacion; }
    public void setFecha_creacion(String value) { this.fecha_creacion = value; }
}
