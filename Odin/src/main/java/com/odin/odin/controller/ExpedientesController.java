package com.odin.odin.controller;

import com.odin.odin.model.Expedientes;
import com.odin.odin.repository.ExpedientesRepository;
import com.odin.odin.service.ExpedientesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expedientes")
@Tag(name="Expedientes", description="Gestión de expedientes documentales")
public class ExpedientesController {
    private final ExpedientesRepository repository; private final ExpedientesService service;
    public ExpedientesController(ExpedientesRepository r, ExpedientesService s) { repository=r; service=s; }
    @GetMapping @Operation(summary="Listar expedientes") public List<Expedientes> all() { return repository.findAll(); }
    @GetMapping("/{id}") @Operation(summary="Consultar expediente por ID") public ResponseEntity<Expedientes> one(@PathVariable Long id) { return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }
    @PostMapping @Operation(summary="Crear expediente") public Expedientes create(@RequestBody Map<String,String> p) { return service.crear(p.get("nombre"), p.get("descripcion")); }
}
