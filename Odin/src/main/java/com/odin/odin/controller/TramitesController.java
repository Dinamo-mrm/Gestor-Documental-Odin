package com.odin.odin.controller;

import com.odin.odin.model.Tramites;
import com.odin.odin.repository.TramitesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tramites")  // ← API REST, no Thymeleaf
public class TramitesController {

    @Autowired
    private TramitesRepository tramitesRepository;

    // GET todos
    @GetMapping
    public List<Tramites> getAll() {
        return tramitesRepository.findAll();
    }

    // GET por id
    @GetMapping("/{id}")
    public ResponseEntity<Tramites> getById(@PathVariable Long id) {
        return tramitesRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST crear
    @PostMapping
    public Tramites create(@RequestBody Tramites tramite) {
        return tramitesRepository.save(tramite);
    }

    // PUT actualizar
    @PutMapping("/{id}")
    public ResponseEntity<Tramites> update(@PathVariable Long id, @RequestBody Tramites tramite) {
        return tramitesRepository.findById(id)
                .map(existing -> {
                    tramite.setIdTramite(id);
                    return ResponseEntity.ok(tramitesRepository.save(tramite));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (tramitesRepository.existsById(id)) {
            tramitesRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}