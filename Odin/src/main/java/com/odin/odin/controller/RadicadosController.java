// RadicadosController.java
package com.odin.odin.controller;

import com.odin.odin.model.Radicados;
import com.odin.odin.repository.RadicadosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/radicados")
public class RadicadosController {

    @Autowired
    private RadicadosRepository radicadosRepository;

    // GET todos
    @GetMapping
    public List<Radicados> getAll() {
        return radicadosRepository.findAll();
    }

    // GET por id
    @GetMapping("/{id}")
    public ResponseEntity<Radicados> getById(@PathVariable Long id) {
        return radicadosRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST crear
    @PostMapping
    public Radicados create(@RequestBody Radicados radicado) {
        return radicadosRepository.save(radicado);
    }

    // PUT actualizar
    @PutMapping("/{id}")
    public ResponseEntity<Radicados> update(@PathVariable Long id, @RequestBody Radicados radicado) {
        return radicadosRepository.findById(id)
                .map(existing -> {
                    radicado.setId_radicado(id);
                    return ResponseEntity.ok(radicadosRepository.save(radicado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (radicadosRepository.existsById(id)) {
            radicadosRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
