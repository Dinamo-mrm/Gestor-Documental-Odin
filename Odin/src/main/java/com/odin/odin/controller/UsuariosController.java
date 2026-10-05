package com.odin.odin.controller;

import com.odin.odin.model.Usuarios;
import com.odin.odin.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name="Usuarios", description="Administración de usuarios del sistema")
public class UsuariosController
{
    @Autowired
    private UsuariosRepository usuariosRepository;

    @GetMapping
    @Operation(summary="Listar usuarios")
    public List<Usuarios> getAll()
    {
        return usuariosRepository.findAll();
    }

    // ✅ CORREGIDO: Añadir @PathVariable
    @GetMapping("/{id}")
    @Operation(summary="Consultar usuario por ID")
    public ResponseEntity<Usuarios> getById(@PathVariable Long id)  // <-- @PathVariable añadido
    {
        return usuariosRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary="Crear usuario")
    public Usuarios create(@RequestBody Usuarios usuarios)
    {
        return usuariosRepository.save(usuarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuarios> update(@PathVariable Long id, @RequestBody Usuarios usuarios)
    {
        return usuariosRepository.findById(id)
                .map(existing -> {
                    usuarios.setId_usuario(id);
                    return ResponseEntity.ok(usuariosRepository.save(usuarios));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id)
    {
        if (usuariosRepository.existsById(id)) {
            usuariosRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}