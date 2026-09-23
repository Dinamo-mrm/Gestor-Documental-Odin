package com.odin.odin.controller;

import com.odin.odin.model.Tramites;
import com.odin.odin.repository.TramitesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tramites")
public class TramitesController {

    @Autowired
    private TramitesRepository tramitesRepository;


    /*
     * =========================================================
     * LISTAR TODOS
     * =========================================================
     */

    @GetMapping
    public List<Tramites> getAll() {

        return tramitesRepository.findAll();
    }


    /*
     * =========================================================
     * BUSCAR POR ID
     * =========================================================
     */

    @GetMapping("/{id}")
    public ResponseEntity<Tramites> getById(
            @PathVariable Long id) {

        return tramitesRepository
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity
                                .notFound()
                                .build()
                );
    }


    /*
     * =========================================================
     * CREAR
     * =========================================================
     */

    @PostMapping
    public Tramites create(
            @RequestBody Tramites tramite) {

        return tramitesRepository.save(tramite);
    }


    /*
     * =========================================================
     * ACTUALIZAR
     * =========================================================
     */

    @PutMapping("/{id}")
    public ResponseEntity<Tramites> update(
            @PathVariable Long id,
            @RequestBody Tramites tramite) {

        return tramitesRepository
                .findById(id)
                .map(existing -> {

                    tramite.setIdTramite(id);

                    return ResponseEntity.ok(
                            tramitesRepository.save(tramite)
                    );
                })
                .orElse(
                        ResponseEntity
                                .notFound()
                                .build()
                );
    }


    /*
     * =========================================================
     * ELIMINAR
     * =========================================================
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        if (!tramitesRepository.existsById(id)) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        tramitesRepository.deleteById(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}