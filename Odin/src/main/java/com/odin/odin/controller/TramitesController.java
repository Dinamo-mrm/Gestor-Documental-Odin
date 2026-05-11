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
    public Tramites create(@RequestBody Tramites usuarios)
    {
        return TramitesRepository.save(Tramites);
    }

    @PutMapping("/{id}")
    public Tramites update(@PathVariable Long id, @RequestBody Tramites tramites)
    {
        Tramites.setId_tramite(id);
        return TramitesRepository.save(Tramites);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id)
    {
        TramitesRepository.deleteById(id);
    }
}
