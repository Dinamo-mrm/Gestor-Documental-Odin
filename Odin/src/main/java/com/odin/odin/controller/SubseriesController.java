package com.odin.odin.controller;

import com.odin.odin.model.Series;
import com.odin.odin.model.Subseries;
import com.odin.odin.repository.SubseriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subseries")
public class SubseriesController
{
    @Autowired
    private SubseriesRepository repository;

    @GetMapping
    public List<Subseries> getAll()
    {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Subseries getById(Long id)
    {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    public Subseries create(@RequestBody Subseries subseries)
    {
        return repository.save(subseries);
    }

    @PutMapping("/{id}")
    public Subseries update(@PathVariable Long id, @RequestBody Subseries subseries)
    {
        subseries.setId_subserie(id);
        return repository.save(subseries);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id)
    {
        repository.deleteById(id);
    }
}
