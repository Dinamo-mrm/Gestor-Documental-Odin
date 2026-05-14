package com.odin.odin.controller;

import com.odin.odin.model.Roles;
import com.odin.odin.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolesController
{
    @Autowired
    private RolesRepository rolesRepository;

    @GetMapping
    public List<Roles> getAll()
    {
        return rolesRepository.findAll();
    }

    @GetMapping("/{id}")
    public Roles getById(@PathVariable Long id) {
        return rolesRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Roles create(@RequestBody Roles roles)
    {
        return rolesRepository.save(roles);
    }

    @PutMapping("/{id}")
    public Roles update(@PathVariable long id, @RequestBody Roles roles)
    {
        roles.setId_rol(id);
        return rolesRepository.save(roles);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id)
    {
        rolesRepository.deleteById(id);
    }
}