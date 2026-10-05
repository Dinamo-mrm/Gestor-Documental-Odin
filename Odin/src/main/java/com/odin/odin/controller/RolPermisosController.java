package com.odin.odin.controller;

import com.odin.odin.model.Permisos;
import com.odin.odin.repository.RolPermisosRepository;
import com.odin.odin.repository.RolesRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roles")
public class RolPermisosController {
    private final RolPermisosRepository repo;
    private final RolesRepository roles;

    public RolPermisosController(RolPermisosRepository repo, RolesRepository roles) {
        this.repo = repo;
        this.roles = roles;
    }

    @GetMapping("/{id}/permisos")
    public ResponseEntity<?> listar(@PathVariable Long id) {
        if (!roles.existsById(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of(
                "rol", id,
                "asignados", repo.buscarPermisosPorRol(id),
                "disponibles", repo.todosLosPermisos()
        ));
    }

    @PutMapping("/{id}/permisos")
    @Transactional
    public ResponseEntity<?> reemplazar(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        if (!roles.existsById(id)) return ResponseEntity.notFound().build();
        Object raw = payload.get("permisos");
        if (!(raw instanceof List<?> lista)) {
            return ResponseEntity.badRequest().body(Map.of("error", "El campo permisos debe ser una lista de IDs"));
        }
        repo.eliminarPermisosDelRol(id);
        for (Object item : lista) {
            try {
                Long permiso = item instanceof Number n ? n.longValue() : Long.valueOf(item.toString());
                repo.asignarPermiso(id, permiso);
            } catch (Exception ignored) { }
        }
        return ResponseEntity.ok(Map.of("rol", id, "asignados", repo.buscarPermisosPorRol(id)));
    }
}
