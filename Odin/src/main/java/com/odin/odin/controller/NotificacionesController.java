package com.odin.odin.controller;

import com.odin.odin.model.Notificaciones;
import com.odin.odin.model.Radicados;
import com.odin.odin.repository.NotificacionesRepository;
import com.odin.odin.repository.RadicadosRepository;
import com.odin.odin.service.OdinUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notificaciones")
@Tag(name = "Notificaciones", description = "Notificaciones del usuario autenticado + alertas de vencimiento")
public class NotificacionesController {

    private final NotificacionesRepository repo;
    private final RadicadosRepository radicadosRepository;

    public NotificacionesController(NotificacionesRepository repo,
                                    RadicadosRepository radicadosRepository) {
        this.repo = repo;
        this.radicadosRepository = radicadosRepository;
    }

    private Long usuario(Authentication auth) {
        if (auth == null || !(auth.getPrincipal() instanceof OdinUserDetails u)) {
            return null;
        }
        return u.getIdUsuario();
    }

    @GetMapping
    @Operation(summary = "Listar notificaciones propias")
    public ResponseEntity<List<Notificaciones>> propias(Authentication auth) {
        Long id = usuario(auth);
        if (id == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(repo.findById_usuarioOrderByFechaDesc(id));
    }

    @GetMapping("/resumen")
    @Operation(summary = "Resumen: notificaciones + radicados vencidos/próximos a vencer")
    public ResponseEntity<List<Notificaciones>> resumen(Authentication auth) {
        Long id = usuario(auth);
        if (id == null) return ResponseEntity.status(401).build();

        List<Notificaciones> out = new ArrayList<>();

        // 1) Notificaciones reales de BD
        try {
            List<Notificaciones> db = repo.findTop10ById_usuarioOrderByFechaDesc(id);
            if (db != null) out.addAll(db);
        } catch (Exception ignored) { }

        // 2) Alertas sintéticas de vencidos (hasta 5)
        try {
            List<Radicados> vencidos = radicadosRepository.findVencidosTop5();
            if (vencidos != null) {
                for (Radicados r : vencidos) {
                    Notificaciones n = new Notificaciones();
                    n.setId_notificacion(-1L * (r.getId_radicado() != null ? r.getId_radicado() : 0));
                    n.setId_usuario(id);
                    n.setId_radicado(r.getId_radicado());
                    n.setTitulo("Radicado vencido");
                    n.setMensaje(safeNum(r) + " — " + safeAsunto(r));
                    n.setLeida(false);
                    n.setFecha(LocalDateTime.now());
                    out.add(n);
                }
            }
        } catch (Exception ignored) { }

        // 3) Próximos a vencer (3 días)
        try {
            List<Radicados> proximos = radicadosRepository.findProximosAVencer(3);
            if (proximos != null) {
                int c = 0;
                for (Radicados r : proximos) {
                    if (c++ >= 5) break;
                    Notificaciones n = new Notificaciones();
                    n.setId_notificacion(-100000L - (r.getId_radicado() != null ? r.getId_radicado() : 0));
                    n.setId_usuario(id);
                    n.setId_radicado(r.getId_radicado());
                    n.setTitulo("Próximo a vencer");
                    n.setMensaje(safeNum(r) + " — límite " + (r.getFecha_limite() != null ? r.getFecha_limite() : r.getFecha_vencimiento()));
                    n.setLeida(false);
                    n.setFecha(LocalDateTime.now());
                    out.add(n);
                }
            }
        } catch (Exception ignored) { }

        // Limitar a 15 ítems
        if (out.size() > 15) {
            out = out.subList(0, 15);
        }
        return ResponseEntity.ok(out);
    }

    @GetMapping("/no-leidas/count")
    public ResponseEntity<Long> noLeidas(Authentication auth) {
        Long id = usuario(auth);
        if (id == null) return ResponseEntity.status(401).build();
        long count = 0;
        try {
            count = repo.countById_usuarioAndLeidaFalse(id);
        } catch (Exception ignored) { }
        try {
            Long v = radicadosRepository.countVencidos();
            if (v != null) count += v;
        } catch (Exception ignored) { }
        return ResponseEntity.ok(count);
    }

    @PutMapping("/{id}/leer")
    @Operation(summary = "Marcar notificación como leída")
    public ResponseEntity<Void> leer(@PathVariable Long id, Authentication auth) {
        Long usuarioId = usuario(auth);
        if (usuarioId == null) return ResponseEntity.status(401).build();
        // IDs sintéticos (negativos) no se marcan en BD
        if (id == null || id < 0) return ResponseEntity.noContent().build();

        Optional<Notificaciones> opt = repo.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Notificaciones n = opt.get();
        if (!usuarioId.equals(n.getId_usuario())) return ResponseEntity.status(403).build();
        n.setLeida(true);
        repo.save(n);
        return ResponseEntity.noContent().build();
    }

    private static String safeNum(Radicados r) {
        return r.getNumero_radicado() != null ? r.getNumero_radicado() : ("#" + r.getId_radicado());
    }

    private static String safeAsunto(Radicados r) {
        String a = r.getAsunto();
        if (a == null || a.isBlank()) return "Sin asunto";
        return a.length() > 60 ? a.substring(0, 59) + "…" : a;
    }
}
