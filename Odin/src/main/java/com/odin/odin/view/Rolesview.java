package com.odin.odin.view;

import com.odin.odin.model.Permisos;
import com.odin.odin.model.Roles;
import com.odin.odin.repository.PermisosRepository;
import com.odin.odin.repository.RolPermisosRepository;
import com.odin.odin.repository.RolesRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class Rolesview {

    private final RolesRepository rolesRepository;
    private final PermisosRepository permisosRepository;
    private final RolPermisosRepository rolPermisosRepository;

    public Rolesview(RolesRepository rolesRepository,
                     PermisosRepository permisosRepository,
                     RolPermisosRepository rolPermisosRepository) {
        this.rolesRepository = rolesRepository;
        this.permisosRepository = permisosRepository;
        this.rolPermisosRepository = rolPermisosRepository;
    }

    @GetMapping("/view/roles")
    public String lista(Model model) {
        model.addAttribute("roles", rolesRepository.findAll());
        return "roles/roles";
    }

    @GetMapping("/view/roles/form")
    public String form(Model model) {
        cargarFormulario(model, new Roles(), new ArrayList<>());
        return "roles/rolesForm";
    }

    @GetMapping("/view/roles/edit/{id}")
    public String edit(@PathVariable Long id,
                       Model model,
                       RedirectAttributes ra) {

        Roles roles = rolesRepository.findById(id).orElse(null);

        if (roles == null) {
            ra.addFlashAttribute("error", "El rol solicitado no existe.");
            return "redirect:/view/roles";
        }

        List<Long> seleccionados = rolPermisosRepository.buscarIdsPermisosPorRol(id);
        cargarFormulario(model, roles, seleccionados);
        return "roles/rolesForm";
    }

    @PostMapping("/view/roles/save")
    @Transactional
    public String save(@ModelAttribute("roles") Roles roles,
                       @RequestParam(name = "permisosSeleccionados", required = false)
                       List<Long> permisosSeleccionados,
                       RedirectAttributes ra) {

        try {
            if (roles.getRol() != null) {
                roles.setRol(roles.getRol().trim().toUpperCase());
            }

            Roles guardado = rolesRepository.save(roles);
            rolesRepository.flush();

            rolPermisosRepository.eliminarPermisosPorRol(guardado.getId_rol());

            if (permisosSeleccionados != null) {
                for (Long idPermiso : permisosSeleccionados) {
                    if (idPermiso != null && permisosRepository.existsById(idPermiso)) {
                        rolPermisosRepository.asignarPermiso(guardado.getId_rol(), idPermiso);
                    }
                }
            }

            ra.addFlashAttribute("mensaje", "Rol y permisos guardados correctamente.");
            return "redirect:/view/roles";

        } catch (Exception e) {
            ra.addFlashAttribute("error", "No fue posible guardar el rol y sus permisos.");
            return roles.getId_rol() == null
                    ? "redirect:/view/roles/form"
                    : "redirect:/view/roles/edit/" + roles.getId_rol();
        }
    }

    @PostMapping("/view/roles/delete/{id}")
    @Transactional
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            rolPermisosRepository.eliminarPermisosPorRol(id);
            rolesRepository.deleteById(id);
            ra.addFlashAttribute("mensaje", "Rol eliminado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("error",
                    "No se puede eliminar el rol porque puede estar asociado a usuarios.");
        }
        return "redirect:/view/roles";
    }

    private void cargarFormulario(Model model,
                                  Roles roles,
                                  List<Long> seleccionados) {
        List<Permisos> permisos = permisosRepository.findAllOrdenados();
        model.addAttribute("roles", roles);
        model.addAttribute("permisos", permisos);

        Map<String, List<Permisos>> permisosPorModulo = new LinkedHashMap<>();
        for (Permisos permiso : permisos) {
            String modulo = permiso.getModulo();
            if (modulo == null || modulo.isBlank()) {
                modulo = "otros";
            }
            permisosPorModulo
                    .computeIfAbsent(modulo, key -> new ArrayList<>())
                    .add(permiso);
        }

        model.addAttribute("permisosPorModulo", permisosPorModulo);
        model.addAttribute("permisosSeleccionados", seleccionados);
        model.addAttribute("totalPermisos", permisos.size());
    }
}
