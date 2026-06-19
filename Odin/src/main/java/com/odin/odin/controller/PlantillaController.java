package com.odin.odin.controller;

import com.odin.odin.model.Plantilla;
import com.odin.odin.repository.PlantillaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plantilla")
public class PlantillaController
{
    @Autowired
    private PlantillaRepository plantillaRepository;

    @GetMapping
    public List<Plantilla> getAll()
    {
        return plantillaRepository.findAll();
    }

    // ✅ CORREGIDO: Añadir @PathVariable
    @GetMapping("/{id}")
    public Plantilla getById(@PathVariable Long id)  // <-- @PathVariable añadido
    {
        return plantillaRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Plantilla create(@RequestBody Plantilla plantilla) {
        return plantillaRepository.save(plantilla);
    }

    @PutMapping("/{id}")
    public Plantilla update(@PathVariable Long id, @RequestBody Plantilla plantilla)
    {
        plantilla.setId_evento(id);
        return plantillaRepository.save(plantilla);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id)
    {
        plantillaRepository.deleteById(id);
    }
}