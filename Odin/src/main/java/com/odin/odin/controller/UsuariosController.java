package com.odin.odin.controller;

import com.odin.odin.model.Usuarios;
import com.odin.odin.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuariosController
{
    @Autowired
    private UsuariosRepository usuariosRepository;

    @GetMapping
    public List<Usuarios> getAll()
    {
        return usuariosRepository.findAll();
    }

    // ✅ CORREGIDO: Añadir @PathVariable
    @GetMapping("/{id}")
    public ResponseEntity<Usuarios> getById(@PathVariable Long id)  // <-- @PathVariable añadido
    {
        return usuariosRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
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