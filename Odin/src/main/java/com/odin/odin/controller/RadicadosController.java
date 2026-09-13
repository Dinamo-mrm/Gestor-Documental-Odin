package com.odin.odin.controller;

import com.odin.odin.model.Radicados;
import com.odin.odin.repository.RadicadosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/radicados")
public class RadicadosController {

    @Autowired
    private RadicadosRepository radicadosRepository;

    @GetMapping
    public List<Radicados> getAll() {
        return radicadosRepository.findAll();
    }

    @GetMapping("/buscar")
    public List<Radicados> buscar(
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) Integer estado,
            @RequestParam(required = false) Long dependencia,
            @RequestParam(required = false) Integer tramite) {
        return radicadosRepository.buscar(texto, estado, dependencia, tramite);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Radicados> getById(@PathVariable Long id) {
        return radicadosRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Radicados create(@RequestBody Radicados radicado) {
        return radicadosRepository.save(radicado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Radicados> update(@PathVariable Long id, @RequestBody Radicados radicado) {
        return radicadosRepository.findById(id)
                .map(existing -> {
                    radicado.setId_radicado(id);
                    return ResponseEntity.ok(radicadosRepository.save(radicado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Radicados> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, Integer> payload) {
        Integer nuevoEstado = payload.get("estado");
        if (nuevoEstado == null) {
            return ResponseEntity.badRequest().build();
        }
        return radicadosRepository.findById(id)
                .map(radicado -> {
                    radicado.setId_estado(nuevoEstado);
                    return ResponseEntity.ok(radicadosRepository.save(radicado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/asignar")
    public ResponseEntity<Radicados> asignar(@PathVariable Long id, @RequestBody Map<String, Integer> payload) {
        Integer usuario = payload.get("usuario");
        if (usuario == null) {
            return ResponseEntity.badRequest().build();
        }
        return radicadosRepository.findById(id)
                .map(radicado -> {
                    radicado.setId_usuario(usuario);
                    return ResponseEntity.ok(radicadosRepository.save(radicado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (radicadosRepository.existsById(id)) {
            radicadosRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
