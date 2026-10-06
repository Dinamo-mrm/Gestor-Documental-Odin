package com.odin.odin.service;

import com.odin.odin.model.Permisos;
import com.odin.odin.model.Roles;
import com.odin.odin.model.Usuarios;
import com.odin.odin.repository.RolesRepository;
import com.odin.odin.repository.RolPermisosRepository;
import com.odin.odin.repository.UsuariosRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service

    private final UsuariosRepository usuariosRepository;
    private final RolesRepository rolesRepository;
    private final RolPermisosRepository rolPermisosRepository;

                                    RolesRepository rolesRepository,
                                    RolPermisosRepository rolPermisosRepository) {
    }

    @Override
        }


        }
        if (usuario.getId_rol() == null) {

        }

        }

                for (Permisos permiso : permisos) {
                    if (!nombrePermiso.isEmpty()) {
                    }
                }
        }

    }

    }
}