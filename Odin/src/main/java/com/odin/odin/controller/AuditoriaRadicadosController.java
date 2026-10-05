package com.odin.odin.controller;

import com.odin.odin.model.AuditoriaRadicados;
import com.odin.odin.service.AuditoriaRadicadosService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/radicados")
public class AuditoriaRadicadosController {
    private final AuditoriaRadicadosService service;
    public AuditoriaRadicadosController(AuditoriaRadicadosService service) { this.service=service; }
    @GetMapping("/{id}/auditoria") public List<AuditoriaRadicados> auditoria(@PathVariable Long id) { return service.porRadicado(id); }
}
