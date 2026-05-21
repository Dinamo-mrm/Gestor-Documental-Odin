package com.odin.odin.controller;

import com.odin.odin.model.Series;
import com.odin.odin.model.Usuarios;
import com.odin.odin.repository.SeriesRepository;
import com.odin.odin.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/series")
public class SeriesController
{
    @Autowired
    private SeriesRepository repository;

    @GetMapping
    public List<Series> getAll()
    {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Series getById(Long id)
    {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    public Series create(@RequestBody Series series)
    {
        return repository.save(series);
    }

    @PutMapping("/{id}")
    public Series update(@PathVariable Long id, @RequestBody Series series)
    {
        series.setId_serie(id);
        return repository.save(series);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id)
    {
        repository.deleteById(id);
    }
}
