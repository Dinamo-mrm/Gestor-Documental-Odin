package com.odin.odin.controller;

import com.odin.odin.model.documentos;
import com.odin.odin.repository.documentosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/documentos")

public class documentosController
{

    @Autowired
    private documentosRepository documentosRepository;

    @GetMapping
    public List<documentos> getAll()
    {
        return documentosRepository.findAll();

    }

    @GetMapping("/{id}")
    public documentos getById(Long id)
    {
        return documentosRepository.findById(id).orElse(null);

    }

    @PostMapping
    public documentos create(@RequestBody documentos Documentos)
    {
        return documentosRepository.save(Documentos);
    }

    @PostMapping("/id")
    public documentos update(@PathVariable Long id, @RequestBody documentos documentos)
    {
        documentos.setId_documento(id);
        return documentosRepository.save(documentos);

    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id)
    {
        documentosRepository.deleteById(id);

    }
}
