package com.odin.odin.service;

import com.odin.odin.model.Permisos;
import com.odin.odin.model.Roles;
import com.odin.odin.model.Usuarios;

import com.odin.odin.repository.RolesRepository;
import com.odin.odin.repository.RolPermisosRepository;
import com.odin.odin.repository.UsuariosRepository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UsuariosRepository usuariosRepository;

    private final RolesRepository rolesRepository;

    private final RolPermisosRepository rolPermisosRepository;


    public CustomUserDetailsService(
            UsuariosRepository usuariosRepository,
            RolesRepository rolesRepository,
            RolPermisosRepository rolPermisosRepository) {

        this.usuariosRepository =
                usuariosRepository;

        this.rolesRepository =
                rolesRepository;

        this.rolPermisosRepository =
                rolPermisosRepository;
    }


    @Override
    public UserDetails loadUserByUsername(
            String correo)
            throws UsernameNotFoundException {

        if (correo == null
                || correo.trim().isEmpty()) {

            throw new UsernameNotFoundException(
                    "Debe ingresar un correo"
            );
        }


        String correoNormalizado =
                correo
                        .trim()
                        .toLowerCase(Locale.ROOT);


        Usuarios usuario =
                usuariosRepository
                        .findByCorreoIgnoreCase(
                                correoNormalizado
                        )
                        .orElseThrow(
                                () ->
                                        new UsernameNotFoundException(
                                                "Usuario no encontrado"
                                        )
                        );


        /*
         * VALIDAR USUARIO ACTIVO
         */

        if (usuario.getEstado() == null
                || !usuario
                .getEstado()
                .trim()
                .equalsIgnoreCase("activo")) {

            throw new UsernameNotFoundException(
                    "Usuario inactivo"
            );
        }


        /*
         * VALIDAR ROL
         */

        if (usuario.getId_rol() == null) {

            throw new UsernameNotFoundException(
                    "El usuario no tiene rol asignado"
            );
        }


        Roles rol =
                rolesRepository
                        .findById(
                                usuario.getId_rol()
                        )
                        .orElseThrow(
                                () ->
                                        new UsernameNotFoundException(
                                                "Rol no encontrado"
                                        )
                        );


        if (rol.getEstado() == null
                || !rol
                .getEstado()
                .trim()
                .equalsIgnoreCase("activo")) {

            throw new UsernameNotFoundException(
                    "El rol se encuentra inactivo"
            );
        }


        /*
         * AUTORIDADES
         */

        List<GrantedAuthority> autoridades =
                new ArrayList<>();


        /*
         * Agregamos ROLE_ADMIN,
         * ROLE_OPERARIO,
         * ROLE_GESTOR,
         * ROLE_CONSULTA...
         */

        String nombreRol =
                normalizarRol(
                        rol.getRol()
                );

        autoridades.add(
                new SimpleGrantedAuthority(
                        "ROLE_" + nombreRol
                )
        );


        /*
         * Ahora cargamos permisos reales
         * desde rol_permisos.
         */

        List<Permisos> permisos =
                rolPermisosRepository
                        .buscarPermisosPorRol(
                                usuario.getId_rol()
                        );


        for (Permisos permiso : permisos) {

            if (permiso.getNombre() == null) {
                continue;
            }

            String nombrePermiso =
                    permiso
                            .getNombre()
                            .trim()
                            .toLowerCase(Locale.ROOT);

            if (!nombrePermiso.isEmpty()) {

                autoridades.add(
                        new SimpleGrantedAuthority(
                                nombrePermiso
                        )
                );
            }
        }


        return User
                .withUsername(
                        usuario.getCorreo()
                )
                .password(
                        usuario.getPassword()
                )
                .authorities(
                        autoridades
                )
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }


    private String normalizarRol(
            String rol) {

        if (rol == null
                || rol.trim().isEmpty()) {

            return "CONSULTA";
        }

        return rol
                .trim()
                .toUpperCase(Locale.ROOT)
                .replace(" ", "_");
    }
}