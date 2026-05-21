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
    @GetMapping("/{id}")
    public Plantilla getById(long id)
    {
        return plantillaRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Plantilla update(@PathVariable long id, @RequestBody Plantilla plantilla)
    {
        plantilla.setId_evento(id);
        return plantillaRepository.save(plantilla);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id)
    {
        plantillaRepository.deleteById(id);
    }


}
