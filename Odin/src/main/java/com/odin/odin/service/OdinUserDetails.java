package com.odin.odin.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class OdinUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final String nombre;
    private final String correo;
    private final Long idUsuario;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean enabled;

    public OdinUserDetails(String username, String password, String nombre, String correo,
                           Long idUsuario, Collection<? extends GrantedAuthority> authorities,
                           boolean enabled) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.correo = correo;
        this.idUsuario = idUsuario;
        this.authorities = authorities;
        this.enabled = enabled;
    }

    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public Long getIdUsuario() { return idUsuario; }

    public String getNombreVisible() {
        if (nombre != null && !nombre.isBlank()) return nombre.trim();
        if (correo != null && !correo.isBlank()) return correo.trim();
        return username != null ? username : "Usuario";
    }

    public String getInicial() {
        String base = getNombreVisible();
        return (base == null || base.isBlank()) ? "U" : base.substring(0, 1).toUpperCase();
    }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return username; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return enabled; }
}
