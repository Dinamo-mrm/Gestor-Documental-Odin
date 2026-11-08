package com.odin.odin.view;

import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Page;

import com.odin.odin.model.Permisos;
import com.odin.odin.model.Roles;
import com.odin.odin.repository.RolesRepository;
import com.odin.odin.repository.RolPermisosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class Rolesview {
    private static final int PAGE_SIZE = 10;


    @Autowired private RolesRepository rolesRepository;
    @Autowired(required = false) private RolPermisosRepository rolPermisosRepository;

    @GetMapping("/view/roles")
    public String lista(@RequestParam(value = "buscar", required = false) String buscar,
                        @RequestParam(value = "estado", required = false) String estado,
                        @RequestParam(defaultValue = "0") int page,
                        Model model) {
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        String b = (buscar != null && !buscar.isBlank()) ? buscar.trim() : null;
        String e = (estado != null && !estado.isBlank() && !"Todos".equalsIgnoreCase(estado)) ? estado.trim() : null;

        Page<Roles> pagina = rolesRepository.buscarConFiltros(b, e, pageable);
        model.addAttribute("roles", pagina.getContent());
        model.addAttribute("page", pagina);
        model.addAttribute("currentPage", pagina.getNumber());
        model.addAttribute("totalPages", Math.max(pagina.getTotalPages(), 1));
        model.addAttribute("totalElements", pagina.getTotalElements());

        // Parámetros de filtros
        model.addAttribute("buscar", buscar != null ? buscar : "");
        model.addAttribute("estado", estado != null ? estado : "");

        return "roles/roles";
    }

    @GetMapping("/view/roles/form")
    public String form(Model model) {
        Roles rol = new Roles();
        rol.setEstado("Activo");
        model.addAttribute("rol", rol);
        model.addAttribute("roles", rol);
        cargarPermisos(model, null);
        return "roles/rolesForm";
    }

    @GetMapping("/view/roles/edit/{id}")
    public String edit(@PathVariable long id, Model model) {
        Roles rol = rolesRepository.findById(id).orElseGet(Roles::new);
        model.addAttribute("rol", rol);
        model.addAttribute("roles", rol);
        cargarPermisos(model, id);
        return "roles/rolesForm";
    }

    @PostMapping({"/view/roles/save", "/view/roles/guardar"})
    public String save(@ModelAttribute("rol") Roles rol,
                       @RequestParam(value = "permisos", required = false) List<Long> permisos,
                       RedirectAttributes ra) {
        if (rol.getId_rol() == null) {
            rol.setEstado("Activo");
        } else if (rol.getEstado() == null || rol.getEstado().isBlank()) {
            Roles existente = rolesRepository.findById(rol.getId_rol()).orElse(null);
            rol.setEstado(existente != null && existente.getEstado() != null ? existente.getEstado() : "Activo");
        }
        if ((rol.getRol() == null || rol.getRol().isBlank()) && rol.getNombre() != null) {
            rol.setRol(rol.getNombre());
        }
        if ((rol.getNombre() == null || rol.getNombre().isBlank()) && rol.getRol() != null) {
            rol.setNombre(rol.getRol());
        }
        Roles saved = rolesRepository.save(rol);

        if (rolPermisosRepository != null && saved.getId_rol() != null) {
            try {
                rolPermisosRepository.eliminarPermisosDelRol(saved.getId_rol());
                if (permisos != null) {
                    for (Long idPermiso : permisos) {
                        if (idPermiso != null) {
                            rolPermisosRepository.asignarPermiso(saved.getId_rol(), idPermiso);
                        }
                    }
                }
            } catch (Exception ignored) {
                // no bloquear guardado del rol si falla la matriz
            }
        }

        ra.addFlashAttribute("mensaje", "Rol guardado correctamente");
        ra.addFlashAttribute("success", "Rol guardado correctamente");
        return "redirect:/view/roles";
    }

    @PostMapping("/view/roles/delete/{id}")
    public String delete(@PathVariable long id, RedirectAttributes ra) {
        rolesRepository.deleteById(id);
        ra.addFlashAttribute("mensaje", "Rol eliminado");
        return "redirect:/view/roles";
    }

    public static class PermisoCard {
        private Long idPermiso;
        private String codigo;
        private String titulo;
        private String descripcion;
        private String categoria;

        public PermisoCard(Long idPermiso, String codigo, String titulo, String descripcion, String categoria) {
            this.idPermiso = idPermiso;
            this.codigo = codigo;
            this.titulo = titulo;
            this.descripcion = descripcion;
            this.categoria = categoria;
        }

        public Long getIdPermiso() { return idPermiso; }
        public String getCodigo() { return codigo; }
        public String getTitulo() { return titulo; }
        public String getDescripcion() { return descripcion; }
        public String getCategoria() { return categoria; }
    }

    private PermisoCard enriquecerPermiso(Permisos p) {
        String code = p.getNombre() != null ? p.getNombre().trim().toLowerCase() : "";
        Long id = p.getId_permiso();

        switch (code) {
            case "ver_radicados":
                return new PermisoCard(id, code, "Consultar Radicados",
                        "Visualizar el listado general y el detalle de documentos radicados en el sistema.",
                        "Radicación de Documentos");
            case "crear_radicado":
                return new PermisoCard(id, code, "Crear Nuevos Radicados",
                        "Registrar y radicar nuevos documentos de entrada o salida con número secuencial único.",
                        "Radicación de Documentos");
            case "editar_radicado":
                return new PermisoCard(id, code, "Editar Información de Radicados",
                        "Modificar metadatos, remitentes y datos básicos de radicados ya existentes.",
                        "Radicación de Documentos");
            case "trasladar_radicado":
                return new PermisoCard(id, code, "Trasladar Radicados entre Dependencias",
                        "Reasignar y transferir la custodia y responsabilidad de radicados hacia otras áreas.",
                        "Radicación de Documentos");
            case "finalizar_radicado":
                return new PermisoCard(id, code, "Finalizar y Cerrar Radicados",
                        "Concluir la gestión documental del radicado y darlo por terminado en el sistema.",
                        "Radicación de Documentos");

            case "admin_usuarios":
                return new PermisoCard(id, code, "Administrar Usuarios del Sistema",
                        "Crear, editar, activar/inactivar y gestionar las cuentas de usuario institucionales.",
                        "Seguridad y Control de Acceso");
            case "admin_roles":
                return new PermisoCard(id, code, "Administrar Roles y Permisos",
                        "Crear y configurar roles y definir qué funciones puede realizar cada perfil.",
                        "Seguridad y Control de Acceso");
            case "ver_usuarios":
                return new PermisoCard(id, code, "Consultar Directorio de Usuarios",
                        "Visualizar el listado de funcionarios registrados en el sistema.",
                        "Seguridad y Control de Acceso");

            case "admin_ccd":
                return new PermisoCard(id, code, "Administrar Cuadro de Clasificación Documental (CCD)",
                        "Crear y gestionar Series, Subseries y la estructura documental de archivo.",
                        "Gestión Documental y Archivo");
            case "gestionar_documentos":
                return new PermisoCard(id, code, "Gestionar Expedientes y Documentos Digitales",
                        "Subir archivos, gestionar anexos digitales y administrar documentos vinculados.",
                        "Gestión Documental y Archivo");

            case "ver_tramites":
                return new PermisoCard(id, code, "Consultar Trámites",
                        "Ver el listado, trazabilidad y estado de los trámites radicados.",
                        "Gestión de Trámites");
            case "gestionar_tramites":
                return new PermisoCard(id, code, "Avanzar y Cambiar Estado de Trámites",
                        "Mover trámites entre etapas de atención, resolver solicitudes y actualizar estados.",
                        "Gestión de Trámites");
            case "asignar_tramites":
                return new PermisoCard(id, code, "Asignar y Reasignar Trámites a Funcionarios",
                        "Delegar la gestión y respuesta de trámites a funcionarios específicos del área.",
                        "Gestión de Trámites");

            case "ver_dependencias":
                return new PermisoCard(id, code, "Consultar Dependencias Institucionales",
                        "Visualizar el catálogo y la estructura de dependencias de la entidad.",
                        "Estructura Organizacional");

            case "ver_reportes":
                return new PermisoCard(id, code, "Generar Reportes y Estadísticas",
                        "Acceder a estadísticas, gráficos, métricas operativas y exportación de datos.",
                        "Reportes y Auditoría");
            case "ver_bitacora":
                return new PermisoCard(id, code, "Consultar Bitácora y Trazabilidad",
                        "Supervisar el historial cronológico y auditoría de eventos ocurridos en el sistema.",
                        "Reportes y Auditoría");

            default:
                String tit = p.getDescripcion() != null && !p.getDescripcion().isBlank() ? p.getDescripcion() : code;
                String mod = p.getModulo() != null && !p.getModulo().isBlank() ? p.getModulo().toUpperCase() : "Otros Permisos";
                return new PermisoCard(id, code, tit, p.getDescripcion(), mod);
        }
    }

    private void cargarPermisos(Model model, Long idRol) {
        List<Permisos> disponibles = Collections.emptyList();
        if (rolPermisosRepository != null) {
            try {
                disponibles = rolPermisosRepository.todosLosPermisos();
            } catch (Exception e) {
                disponibles = Collections.emptyList();
            }
        }
        model.addAttribute("permisosDisponibles", disponibles != null ? disponibles : Collections.emptyList());

        Map<String, List<PermisoCard>> permisosPorCategoria = new LinkedHashMap<>();
        permisosPorCategoria.put("Seguridad y Control de Acceso", new ArrayList<>());
        permisosPorCategoria.put("Radicación de Documentos", new ArrayList<>());
        permisosPorCategoria.put("Gestión de Trámites", new ArrayList<>());
        permisosPorCategoria.put("Gestión Documental y Archivo", new ArrayList<>());
        permisosPorCategoria.put("Estructura Organizacional", new ArrayList<>());
        permisosPorCategoria.put("Reportes y Auditoría", new ArrayList<>());

        if (disponibles != null) {
            for (Permisos p : disponibles) {
                PermisoCard card = enriquecerPermiso(p);
                permisosPorCategoria.computeIfAbsent(card.getCategoria(), k -> new ArrayList<>()).add(card);
            }
        }
        permisosPorCategoria.entrySet().removeIf(e -> e.getValue().isEmpty());
        model.addAttribute("permisosPorCategoria", permisosPorCategoria);

        Set<Long> asignados = new HashSet<>();
        if (idRol != null && rolPermisosRepository != null) {
            try {
                List<Permisos> delRol = rolPermisosRepository.buscarPermisosPorRol(idRol);
                if (delRol != null) {
                    asignados = delRol.stream()
                            .map(Permisos::getId_permiso)
                            .filter(x -> x != null)
                            .collect(Collectors.toSet());
                }
            } catch (Exception ignored) {
                asignados = new HashSet<>();
            }
        }
        model.addAttribute("permisosAsignados", asignados);
    }
}
