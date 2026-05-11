package com.odin.odin.controller;

import com.odin.odin.model.Documentos;
import com.odin.odin.repository.DocumentosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/documentos")
public class DocumentosController
{

    @Autowired
    private DocumentosRepository documentosRepository;

    @GetMapping
    public List<Documentos> getAll()
    {
        return documentosRepository.findAll();

    }

    @GetMapping("/{id}")
    public Documentos getById(@PathVariable Long id)
    {
        return documentosRepository.findById(id).orElse(null);

    }

    @PostMapping
    public Documentos create(@RequestBody Documentos Documentos)
    {
        return documentosRepository.save(Documentos);
    }

    @PostMapping("/id")
    public Documentos update(@PathVariable Long id, @RequestBody Documentos documentos)
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
