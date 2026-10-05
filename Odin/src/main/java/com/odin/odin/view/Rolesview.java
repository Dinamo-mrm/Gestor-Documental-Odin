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

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class Rolesview {
    private static final int PAGE_SIZE = 10;


    @Autowired private RolesRepository rolesRepository;
    @Autowired(required = false) private RolPermisosRepository rolPermisosRepository;

    @GetMapping("/view/roles")
    public String lista(@RequestParam(defaultValue = "0") int page, Model model) {
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<?> pagina = rolesRepository.findAll(pageable);
        model.addAttribute("roles", pagina.getContent());
        model.addAttribute("page", pagina);
        model.addAttribute("currentPage", pagina.getNumber());
        model.addAttribute("totalPages", Math.max(pagina.getTotalPages(), 1));
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
        if (rol.getEstado() == null || rol.getEstado().isBlank()) {
            rol.setEstado("Activo");
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
