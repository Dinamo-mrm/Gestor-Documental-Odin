package com.odin.odin.view;

import com.odin.odin.model.Dependencias;
import com.odin.odin.model.Roles;
import com.odin.odin.model.Usuarios;
import com.odin.odin.repository.DependenciasRepository;
import com.odin.odin.repository.RolesRepository;
import com.odin.odin.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UsuariosView {
    private static final int PAGE_SIZE = 10;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private DependenciasRepository dependenciasRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/view/usuarios")
    public String lista(@RequestParam(value = "buscar", required = false) String buscar,
                        @RequestParam(value = "tipoId", required = false) String tipoId,
                        @RequestParam(value = "estado", required = false) String estado,
                        @RequestParam(value = "idDependencia", required = false) Long idDependencia,
                        @RequestParam(defaultValue = "0") int page,
                        Model model) {
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        String b = (buscar != null && !buscar.isBlank()) ? buscar.trim() : null;
        String t = (tipoId != null && !tipoId.isBlank() && !"Todos los tipos".equalsIgnoreCase(tipoId)) ? tipoId.trim() : null;
        String e = (estado != null && !estado.isBlank() && !"Estado".equalsIgnoreCase(estado) && !"Todos".equalsIgnoreCase(estado)) ? estado.trim() : null;
        Long dep = (idDependencia != null && idDependencia > 0) ? idDependencia : null;

        Page<Usuarios> pagina = usuariosRepository.buscarConFiltros(b, t, e, dep, pageable);
        model.addAttribute("usuarios", pagina.getContent());
        model.addAttribute("page", pagina);
        model.addAttribute("currentPage", pagina.getNumber());
        model.addAttribute("totalPages", Math.max(pagina.getTotalPages(), 1));
        model.addAttribute("totalElements", pagina.getTotalElements());

        // Parámetros de filtros para preservar en el formulario
        model.addAttribute("buscar", buscar != null ? buscar : "");
        model.addAttribute("tipoId", tipoId != null ? tipoId : "");
        model.addAttribute("estado", estado != null ? estado : "");
        model.addAttribute("idDependencia", idDependencia);
        model.addAttribute("listaDependencias", dependenciasRepository.findAll());

        // Mapas para mostrar nombres de rol y dependencia en la tabla
        Map<Long, String> nombresRoles = new HashMap<>();
        rolesRepository.findAll().forEach(r -> {
            if (r.getId_rol() != null) {
                String nombre = (r.getNombre() != null && !r.getNombre().isBlank()) ? r.getNombre() : r.getRol();
                nombresRoles.put(r.getId_rol(), nombre != null ? nombre : ("Rol #" + r.getId_rol()));
            }
        });
        Map<Long, String> nombresDep = new HashMap<>();
        dependenciasRepository.findAll().forEach(d -> {
            if (d.getId_dependencia() != null) {
                String nombre = d.getNombre() != null ? d.getNombre() : d.getDependencia();
                nombresDep.put(d.getId_dependencia(), nombre != null ? nombre : ("Dep #" + d.getId_dependencia()));
            }
        });
        model.addAttribute("nombresRoles", nombresRoles);
        model.addAttribute("nombresDep", nombresDep);

        return "usuarios/usuarios";
    }

    @GetMapping("/view/usuarios/form")
    public String form(Model model) {
        Usuarios usuario = new Usuarios();
        usuario.setEstado("Activo"); // Al crear un nuevo usuario queda activo por defecto
        model.addAttribute("usuarios", usuario);
        model.addAttribute("listaRoles", rolesRepository.findAll());
        model.addAttribute("listaDependencias", dependenciasRepository.findAll());
        return "usuarios/usuariosForm";
    }

    @PostMapping("/view/usuarios/save")
    public String save(@ModelAttribute Usuarios usuarios, RedirectAttributes ra) {
        if (usuarios.getId_usuario() != null) {
            // Edición
            Usuarios existente = usuariosRepository.findById(usuarios.getId_usuario()).orElse(null);
            if (existente != null) {
                if (usuarios.getPassword() == null || usuarios.getPassword().isBlank()) {
                    usuarios.setPassword(existente.getPassword());
                } else if (!usuarios.getPassword().startsWith("$2a$") && !usuarios.getPassword().startsWith("$2b$")) {
                    usuarios.setPassword(passwordEncoder.encode(usuarios.getPassword()));
                }
                if (usuarios.getFecha_creacion() == null) {
                    usuarios.setFecha_creacion(existente.getFecha_creacion());
                }
                if (usuarios.getEstado() == null || usuarios.getEstado().isBlank()) {
                    usuarios.setEstado(existente.getEstado() != null ? existente.getEstado() : "Activo");
                }
            }
        } else {
            // Nuevo usuario: siempre queda Activo de forma automática
            usuarios.setEstado("Activo");
            // La contraseña inicial por defecto es su número de identificación (cédula)
            String claveInicial = (usuarios.getNum_identificacion() != null && !usuarios.getNum_identificacion().isBlank())
                    ? usuarios.getNum_identificacion().trim()
                    : "123456";
            usuarios.setPassword(passwordEncoder.encode(claveInicial));
            if (usuarios.getFecha_creacion() == null) {
                usuarios.setFecha_creacion(LocalDateTime.now().toString());
            }
        }

        usuariosRepository.save(usuarios);
        ra.addFlashAttribute("mensaje", "Usuario registrado con éxito");
        ra.addFlashAttribute("success", "Usuario guardado con éxito");
        return "redirect:/view/usuarios";
    }

    @GetMapping("/view/usuarios/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Usuarios usuarios = usuariosRepository.findById(id).orElse(null);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("listaRoles", rolesRepository.findAll());
        model.addAttribute("listaDependencias", dependenciasRepository.findAll());
        return "usuarios/usuariosForm";
    }

    @PostMapping("/view/usuarios/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        usuariosRepository.deleteById(id);
        ra.addFlashAttribute("mensaje", "Usuario eliminado con éxito");
        return "redirect:/view/usuarios";
    }
}
