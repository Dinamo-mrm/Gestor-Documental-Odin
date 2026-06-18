package com.odin.odin.controller;

import com.odin.odin.model.Tramites;
import com.odin.odin.repository.TramitesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tramites")
public class TramitesController {

    @Autowired
    private TramitesRepository tramitesRepository;

    @GetMapping
    public List<Tramites> getAll() {
        return tramitesRepository.findAll();
    }

    @GetMapping("/{id}")
    public Tramites getById(@PathVariable Long id) {

        return tramitesRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Trámite no encontrado"));
    }

    @PostMapping
    public Tramites create(@RequestBody Tramites tramite) {
        return tramitesRepository.save(tramite);
    }

    @PutMapping("/{id}")
    public Tramites update(
            @PathVariable Long id,
            @RequestBody Tramites tramite) {

        Tramites existente = tramitesRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Trámite no encontrado"));

        existente.setNombre(tramite.getNombre());
        existente.setDescripcion(tramite.getDescripcion());
        existente.setIdDependenciaResponsable(
                tramite.getIdDependenciaResponsable());
        existente.setIdEstadoInicial(
                tramite.getIdEstadoInicial());
        existente.setDiasRespuesta(
                tramite.getDiasRespuesta());
        existente.setPrioridadDefault(
                tramite.getPrioridadDefault());
        existente.setRequiereRespuesta(
                tramite.getRequiereRespuesta());
        existente.setActivo(
                tramite.getActivo());

        return tramitesRepository.save(existente);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        tramitesRepository.deleteById(id);
    }
}