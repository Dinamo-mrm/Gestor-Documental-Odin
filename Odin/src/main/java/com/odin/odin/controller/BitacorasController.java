package com.odin.odin.controller;
import com.odin.odin.model.Bitacoras;
import com.odin.odin.repository.BitacorasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bitacoras")
public class BitacorasController
{
    @Autowired
    private BitacorasRepository bitacorasRepository;

    @GetMapping
    public List<Bitacoras> getAll()
    {
        return bitacorasRepository.findAll();

    }
    @GetMapping("/{id}")
    public Bitacoras getById(long id)
    {
        return bitacorasRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Bitacoras update(@PathVariable long id, @RequestBody Bitacoras bitacoras)
    {
        bitacoras.setId_bitacora(id);
        return bitacorasRepository.save(bitacoras);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id)
    {
        bitacorasRepository.deleteById(id);
    }


}
