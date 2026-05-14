package com.odin.odin.controller;

import com.odin.odin.model.Tramites;
import com.odin.odin.repository.TramitesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tramites")
public class TramitesController
{
    @Autowired
    private TramitesRepository TramitesRepository;

    @GetMapping
    public List<Tramites> getAll()
    {
        return TramitesRepository.findAll();
    }

    @GetMapping("/{id}")
    public Tramites getById(Long id)
    {
        return TramitesRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Tramites create(@RequestBody Tramites tramites)
    {
        return TramitesRepository.save(tramites);
    }

    @PutMapping("/{id}")
    public Tramites update(@PathVariable Long id, @RequestBody Tramites tramites)
    {
        tramites.setId_tramite(id);
        return TramitesRepository.save(tramites);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id)
    {
        TramitesRepository.deleteById(id);
    }
}
