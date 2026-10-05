package com.odin.odin.controller;

import com.odin.odin.model.Documentos;
import com.odin.odin.model.VersionesDocumento;
import com.odin.odin.repository.DocumentosRepository;
import com.odin.odin.service.DocumentosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/documentos")
@Tag(name="Documentos", description="Documentos y versiones documentales")
public class DocumentosController {
    private final DocumentosRepository documentosRepository;
    private final DocumentosService documentosService;
    public DocumentosController(DocumentosRepository r, DocumentosService s) { this.documentosRepository=r; this.documentosService=s; }

    @GetMapping @Operation(summary="Listar documentos") public List<Documentos> getAll() { return documentosRepository.findAll(); }
    @GetMapping("/{id}") public ResponseEntity<Documentos> getById(@PathVariable Long id) {
        return documentosRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/radicado/{radicado}")
    @Operation(summary="Consultar documentos de un radicado") public List<Documentos> porRadicado(@PathVariable Long radicado) {
        return documentosRepository.buscarPorRadicado(radicado);
    }
    @GetMapping("/{id}/versiones")
    @Operation(summary="Consultar versiones de un documento") public List<VersionesDocumento> versiones(@PathVariable Long id) {
        return documentosService.versiones(id);
    }
    @PostMapping public Documentos create(@RequestBody Documentos documento,
                                          @RequestHeader(value="X-User-Id", required=false) Long usuario) {
        return documentosService.guardar(documento, usuario, "Versión inicial");
    }
    @PostMapping("/{id}/versiones") public ResponseEntity<?> nuevaVersion(
            @PathVariable Long id, @RequestBody Map<String,Object> payload,
            @RequestHeader(value="X-User-Id", required=false) Long usuario) {
        try {
            String ruta = payload.get("ruta_archivo") == null ? null : payload.get("ruta_archivo").toString();
            String observacion = payload.get("observacion") == null ? null : payload.get("observacion").toString();
            return ResponseEntity.ok(documentosService.crearNuevaVersion(id, ruta, observacion, usuario));
        } catch (IllegalArgumentException e) { return ResponseEntity.notFound().build(); }
    }
    @PutMapping("/{id}") public ResponseEntity<Documentos> update(@PathVariable Long id, @RequestBody Documentos d) {
        if (!documentosRepository.existsById(id)) return ResponseEntity.notFound().build();
        d.setId_documento(id); return ResponseEntity.ok(documentosRepository.save(d));
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!documentosRepository.existsById(id)) return ResponseEntity.notFound().build();
        documentosRepository.deleteById(id); return ResponseEntity.noContent().build();
    }
}
