package com.odin.odin.controller;

import com.odin.odin.model.Radicados;
import com.odin.odin.model.Reasignaciones;
import com.odin.odin.repository.RadicadosRepository;
import com.odin.odin.repository.ReasignacionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/radicados")
public class RadicadosController {

    @Autowired
    private RadicadosRepository radicadosRepository;

    @Autowired
    private ReasignacionesRepository reasignacionesRepository;

    @GetMapping
    public List<Radicados> getAll() {
        return radicadosRepository.findAll();
    }

    @GetMapping("/buscar")
    public List<Radicados> buscar(
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) Integer estado,
            @RequestParam(required = false) Long dependencia,
            @RequestParam(required = false) Integer tramite) {
        return radicadosRepository.buscar(texto, estado, dependencia, tramite);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Radicados> getById(@PathVariable Long id) {
        return radicadosRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Radicados create(@RequestBody Radicados radicado) {
        return radicadosRepository.save(radicado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Radicados> update(@PathVariable Long id, @RequestBody Radicados radicado) {
        return radicadosRepository.findById(id)
                .map(existing -> {
                    radicado.setId_radicado(id);
                    return ResponseEntity.ok(radicadosRepository.save(radicado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Radicados> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, Integer> payload) {
        Integer nuevoEstado = payload.get("estado");
        if (nuevoEstado == null || nuevoEstado <= 0) {
            return ResponseEntity.badRequest().build();
        }
        return radicadosRepository.findById(id)
                .map(radicado -> {
                    radicado.setId_estado(nuevoEstado);
                    return ResponseEntity.ok(radicadosRepository.save(radicado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/asignar")
    public ResponseEntity<Radicados> asignar(@PathVariable Long id, @RequestBody Map<String, Integer> payload) {
        Integer usuario = payload.get("usuario");
        if (usuario == null || usuario <= 0) {
            return ResponseEntity.badRequest().build();
        }
        return radicadosRepository.findById(id)
                .map(radicado -> {
                    radicado.setId_usuario(usuario);
                    return ResponseEntity.ok(radicadosRepository.save(radicado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/reasignar")
    public ResponseEntity<?> reasignar(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        Integer usuarioNuevo = numero(payload.get("usuarioNuevo"));
        Integer dependenciaNueva = numero(payload.get("dependenciaNueva"));

        if (usuarioNuevo == null || usuarioNuevo <= 0 || dependenciaNueva == null || dependenciaNueva <= 0) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "usuarioNuevo y dependenciaNueva son obligatorios y deben ser mayores que cero"
            ));
        }

        return radicadosRepository.findById(id)
                .map(radicado -> {
                    Integer usuarioAnterior = radicado.getId_usuario();
                    Integer dependenciaAnterior = radicado.getId_dependencia() == null
                            ? null
                            : radicado.getId_dependencia().intValue();

                    int actualizados = radicadosRepository.actualizarAsignacion(
                            id, usuarioNuevo, dependenciaNueva);

                    if (actualizados == 0) {
                        return ResponseEntity.internalServerError().body(Map.of(
                                "error", "No fue posible actualizar la asignación del radicado"
                        ));
                    }

                    Reasignaciones historial = new Reasignaciones();
                    historial.setId_radicado(id.intValue());
                    historial.setId_usuario_anterior(usuarioAnterior);
                    historial.setId_usuario_nuevo(usuarioNuevo);
                    historial.setId_dependencia_nueva(dependenciaNueva);
                    historial.setFecha(LocalDateTime.now().toString());
                    reasignacionesRepository.save(historial);

                    Radicados actualizado = radicadosRepository.findById(id).orElse(radicado);
                    return ResponseEntity.ok(actualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/cerrar")
    public ResponseEntity<?> cerrar(@PathVariable Long id, @RequestBody(required = false) Map<String, Object> payload) {
        Integer estadoCierre = payload == null ? null : numero(payload.get("estado"));
        if (estadoCierre == null || estadoCierre <= 0) {
            estadoCierre = 3;
        }

        final Integer estadoFinal = estadoCierre;
        return radicadosRepository.findById(id)
                .map(radicado -> {
                    radicado.setId_estado(estadoFinal);
                    Radicados cerrado = radicadosRepository.save(radicado);
                    return ResponseEntity.ok(cerrado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (radicadosRepository.existsById(id)) {
            radicadosRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private Integer numero(Object valor) {
        if (valor == null) {
            return null;
        }
        if (valor instanceof Number) {
            return ((Number) valor).intValue();
        }
        try {
            return Integer.valueOf(valor.toString().trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
