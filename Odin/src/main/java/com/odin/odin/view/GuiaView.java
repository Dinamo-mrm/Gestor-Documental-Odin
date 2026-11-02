package com.odin.odin.view;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador MVC para el módulo independiente "Guía Interactiva de ODIN".
 *
 * Propósito académico y funcional:
 * - Expone la ruta GET /view/guia para cualquier usuario que haya iniciado sesión.
 * - Consulta los permisos (authorities) de la sesión actual en Spring Security
 *   sin modificar ni escribir absolutamente nada en la base de datos.
 * - Envía esos permisos a la vista Thymeleaf (guia/guia.html) para que la guía
 *   indique qué módulos están habilitados para el rol del usuario actual y cuáles
 *   se muestran en modo informativo/aprendizaje.
 */
@Controller
@RequestMapping("/view/guia")
public class GuiaView {

    @GetMapping
    public String mostrarGuiaInteractiva(Model model) {
        List<String> permisosUsuario = new ArrayList<>();
        boolean esAdmin = false;

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && auth.getAuthorities() != null) {
                permisosUsuario = auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .filter(a -> a != null && !a.isBlank())
                        .collect(Collectors.toList());

                esAdmin = permisosUsuario.contains("ROLE_ADMIN")
                        || permisosUsuario.contains("ROLE_ROOT");
            }
        } catch (Exception ignored) {
            permisosUsuario = Collections.emptyList();
        }

        // Cadena separada por comas para que el archivo JS externo lea los permisos desde un atributo data-*
        String permisosCsv = String.join(",", permisosUsuario);

        // Determinar una etiqueta amigable del perfil actual según sus permisos
        String perfilDetectado = detectarPerfilAmigable(permisosUsuario, esAdmin);

        model.addAttribute("permisosUsuario", permisosUsuario);
        model.addAttribute("permisosCsv", permisosCsv);
        model.addAttribute("esAdminGuia", esAdmin);
        model.addAttribute("perfilDetectado", perfilDetectado);

        return "guia/guia";
    }

    /**
     * Determina el nombre amigable del perfil del usuario autenticado
     * a partir de sus permisos reales cargados por Spring Security.
     */
    private String detectarPerfilAmigable(List<String> permisos, boolean esAdmin) {
        if (esAdmin) {
            return "Administrador / Root (Acceso total)";
        }
        if (permisos.contains("admin_usuarios") || permisos.contains("admin_roles")) {
            return "Administrador de Usuarios y Roles";
        }
        if (permisos.contains("gestionar_tramites") || permisos.contains("trasladar_radicado")) {
            return "Gestor de Trámites";
        }
        if (permisos.contains("crear_radicado") || permisos.contains("editar_radicado")) {
            return "Operario / Recepción (Radicador)";
        }
        if (permisos.contains("ver_radicados") || permisos.contains("ver_reportes")) {
            return "Consulta y Seguimiento";
        }
        return "Usuario Autenticado";
    }
}
