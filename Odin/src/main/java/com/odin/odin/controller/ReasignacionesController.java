package com.odin.odin.controller;

import com.odin.odin.model.Reasignaciones;
import com.odin.odin.repository.ReasignacionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reasignaciones")
public class ReasignacionesController {

    @Autowired
    private ReasignacionesRepository reasignacionesRepository;

    @GetMapping
    public List<Reasignaciones> getAll() {
        return reasignacionesRepository.findAll();
    }

    @GetMapping("/{id}")
    public Reasignaciones getById(@PathVariable Long id) {
        return reasignacionesRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Reasignaciones create(@RequestBody Reasignaciones reasignaciones) {
        return reasignacionesRepository.save(reasignaciones);
    }

    @PutMapping("/{id}")
    public Reasignaciones update(@PathVariable Long id, @RequestBody Reasignaciones reasignaciones) {
        reasignaciones.setId_reasignacion(id);
        return reasignacionesRepository.save(reasignaciones);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        reasignacionesRepository.deleteById(id);
    }
}