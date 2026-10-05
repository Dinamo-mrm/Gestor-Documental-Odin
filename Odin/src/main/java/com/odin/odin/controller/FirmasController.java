package com.odin.odin.controller;

import com.odin.odin.model.Firmas;
import com.odin.odin.repository.FirmasRepository;
import com.odin.odin.service.OdinUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/firmas")
@Tag(name = "Firmas", description = "Registro y consulta de firmas asociadas a radicados")
public class FirmasController {

    private final FirmasRepository repo;

    public FirmasController(FirmasRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/radicado/{id}")
    @Operation(summary = "Consultar firmas de un radicado")
    public List<Firmas> porRadicado(@PathVariable Long id) {
        return repo.findByIdRadicadoOrderByFechaFirmaDesc(id);
    }

    @PostMapping
    @Operation(summary = "Registrar solicitud/firma pendiente")
    public ResponseEntity<?> registrar(@RequestBody Map<String, Object> body, Authentication auth) {
        Long usuarioAuth = usuarioAutenticado(auth);
        if (usuarioAuth == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Debe iniciar sesión para solicitar firma"));
        }

        Long idRadicado = numero(body.get("idRadicado"));
        if (idRadicado == null) idRadicado = numero(body.get("id_radicado"));
        if (idRadicado == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "id_radicado es obligatorio"));
        }

        Long idUsuario = numero(body.get("idUsuario"));
        if (idUsuario == null) idUsuario = numero(body.get("id_usuario"));
        if (idUsuario == null) idUsuario = usuarioAuth; // firma del usuario autenticado

        String estado = texto(body.get("estado"));
        if (estado.isBlank()) estado = "PENDIENTE";

        String observacion = texto(body.get("observacion"));
        if (observacion.isBlank()) observacion = "Solicitud de firma registrada";

        Firmas f = Firmas.builder()
                .idRadicado(idRadicado)
                .idUsuario(idUsuario)
                .fechaFirma(LocalDateTime.now())
                .estado(estado)
                .observacion(observacion)
                .build();

        Firmas saved = repo.save(f);
        return ResponseEntity.ok(Map.of(
                "idFirma", saved.getIdFirma(),
                "idRadicado", saved.getIdRadicado(),
                "idUsuario", saved.getIdUsuario(),
                "estado", saved.getEstado(),
                "mensaje", "Firma/solicitud registrada"
        ));
    }

    private Long usuarioAutenticado(Authentication auth) {
        if (auth == null || !(auth.getPrincipal() instanceof OdinUserDetails u)) return null;
        return u.getIdUsuario();
    }

    private Long numero(Object v) {
        try {
            if (v == null) return null;
            if (v instanceof Number n) return n.longValue();
            String s = v.toString().trim();
            if (s.isEmpty()) return null;
            return Long.valueOf(s);
        } catch (Exception e) {
            return null;
        }
    }

    private String texto(Object v) {
        return v == null ? "" : v.toString().trim();
    }
}
