package com.odin.odin.view;

import com.odin.odin.dto.TimelineEvent;
import com.odin.odin.model.*;
import com.odin.odin.repository.*;
import com.odin.odin.util.PlazoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Detalle de radicado con línea temporal unificada:
 * Radicado → eventos → usuario → dependencia → estado → documento → observación.
 */
@Controller
public class RadicadoDetalleView {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Autowired private RadicadosRepository radicadosRepository;
    @Autowired private DocumentosRepository documentosRepository;
    @Autowired private HistorialRadicadoRepository historialRepository;
    @Autowired private ObservacionesRepository observacionesRepository;
    @Autowired private AuditoriaRadicadosRepository auditoriaRepository;
    @Autowired private FirmasRepository firmasRepository;
    @Autowired private AnexosRepository anexosRepository;
    @Autowired private DependenciasRepository dependenciasRepository;
    @Autowired private EstadosRepository estadosRepository;
    @Autowired private TramitesRepository tramitesRepository;
    @Autowired private UsuariosRepository usuariosRepository;
    @Autowired private RolesRepository rolesRepository;
    @Autowired private ReasignacionesRepository reasignacionesRepository;

    @GetMapping("/view/radicados/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        var opt = radicadosRepository.findById(id);
        if (opt.isEmpty()) {
            model.addAttribute("error", "Radicado no encontrado: " + id);
            model.addAttribute("radicado", null);
            model.addAttribute("documentos", List.of());
            model.addAttribute("historial", List.of());
            model.addAttribute("observaciones", List.of());
            model.addAttribute("auditoria", List.of());
            model.addAttribute("firmas", List.of());
            model.addAttribute("anexos", List.of());
            model.addAttribute("timeline", List.of());
            model.addAttribute("usuarios", usuariosRepository.findAll());
            return "radicados/radicado-detalle";
        }

        Radicados radicado = opt.get();
        Integer diasTr = null;
        if (radicado.getId_tramite() != null) {
            diasTr = tramitesRepository.findById(radicado.getId_tramite())
                    .map(tr -> tr.getDiasRespuesta()).orElse(null);
        }
        PlazoUtil.aplicar(radicado, diasTr);

        Map<Long, String> nombresEstado = new HashMap<>();
        estadosRepository.findAll().forEach(e -> {
            if (e.getId_estado() != null)
                nombresEstado.put(e.getId_estado(), e.getNombre() != null ? e.getNombre() : String.valueOf(e.getId_estado()));
        });
        Map<Long, String> nombresDep = new HashMap<>();
        dependenciasRepository.findAll().forEach(d -> {
            if (d.getId_dependencia() != null)
                nombresDep.put(d.getId_dependencia(), d.getNombre() != null ? d.getNombre() : String.valueOf(d.getId_dependencia()));
        });
        Map<Long, String> nombresTramite = new HashMap<>();
        tramitesRepository.findAll().forEach(tr -> {
            if (tr.getIdTramite() != null)
                nombresTramite.put(tr.getIdTramite(), tr.getNombre() != null ? tr.getNombre() : String.valueOf(tr.getIdTramite()));
        });
        Map<Long, String> nombresUsuario = new HashMap<>();
        usuariosRepository.findAll().forEach(u -> {
            if (u.getId_usuario() != null)
                nombresUsuario.put(u.getId_usuario(), u.getNombre() != null ? u.getNombre() : String.valueOf(u.getId_usuario()));
        });

        List<?> documentos = safeList(() -> documentosRepository.buscarPorRadicado(id));
        List<?> historial = safeList(() -> historialRepository.findByRadicadoOrderByFechaDesc(id));
        List<?> observaciones = safeList(() -> observacionesRepository.findByRadicadoOrderByFechaDesc(id));
        List<?> auditoria = safeList(() -> auditoriaRepository.findById_radicadoOrderByFechaDesc(id));
        List<?> firmas = safeList(() -> firmasRepository.findByIdRadicadoOrderByFechaFirmaDesc(id));
        List<?> anexos = safeList(() -> anexosRepository.findByIdRadicadoOrderByIdAnexoDesc(id));
        List<?> reasignaciones = safeList(() -> reasignacionesRepository.findByRadicadoOrderByFechaDesc(id));

        List<TimelineEvent> timeline = construirTimeline(
                radicado, historial, observaciones, documentos, firmas, anexos, reasignaciones,
                nombresUsuario, nombresDep, nombresEstado);

        model.addAttribute("radicado", radicado);
        model.addAttribute("nombresEstado", nombresEstado);
        model.addAttribute("nombresDep", nombresDep);
        model.addAttribute("nombresTramite", nombresTramite);
        model.addAttribute("nombresUsuario", nombresUsuario);
        model.addAttribute("usuarios", usuariosRepository.findAll());
        model.addAttribute("documentos", documentos);
        model.addAttribute("historial", historial);
        model.addAttribute("observaciones", observaciones);
        model.addAttribute("auditoria", auditoria);
        model.addAttribute("firmas", firmas);
        model.addAttribute("anexos", anexos);
        model.addAttribute("timeline", timeline);

        return "radicados/radicado-detalle";
    }

    @SuppressWarnings("unchecked")
    private List<TimelineEvent> construirTimeline(
            Radicados radicado,
            List<?> historial,
            List<?> observaciones,
            List<?> documentos,
            List<?> firmas,
            List<?> anexos,
            List<?> reasignaciones,
            Map<Long, String> nombresUsuario,
            Map<Long, String> nombresDep,
            Map<Long, String> nombresEstado) {

        List<TimelineEvent> events = new ArrayList<>();

        // Evento raíz: creación / radicación
        LocalDateTime fechaRad = parseFlexible(radicado.getFecha_radicado());
        String estadoActual = radicado.getId_estado() != null && nombresEstado.containsKey(radicado.getId_estado())
                ? nombresEstado.get(radicado.getId_estado())
                : (radicado.getId_estado() != null ? String.valueOf(radicado.getId_estado()) : "—");
        String depActual = radicado.getId_dependencia() != null && nombresDep.containsKey(radicado.getId_dependencia())
                ? nombresDep.get(radicado.getId_dependencia())
                : "—";
        String respActual = radicado.getId_usuario() != null && nombresUsuario.containsKey(radicado.getId_usuario())
                ? nombresUsuario.get(radicado.getId_usuario())
                : "—";
        events.add(new TimelineEvent(
                fechaRad,
                formatFecha(fechaRad, radicado.getFecha_radicado()),
                "RADICACION",
                "Radicación del documento",
                radicado.getAsunto() != null ? radicado.getAsunto() : "Documento radicado",
                respActual,
                "Estado: " + estadoActual + " · Dependencia: " + depActual,
                "tl-radicacion"
        ));

        for (Object o : historial) {
            if (!(o instanceof HistorialRadicado h)) continue;
            String user = nombreUsuario(h.getId_usuario(), nombresUsuario);
            events.add(new TimelineEvent(
                    h.getFecha(),
                    formatFecha(h.getFecha(), null),
                    "HISTORIAL",
                    h.getAccion() != null ? h.getAccion() : "Movimiento",
                    h.getDescripcion(),
                    user,
                    null,
                    "tl-historial"
            ));
        }

        for (Object o : observaciones) {
            if (!(o instanceof Observaciones obs)) continue;
            String user = nombreUsuario(obs.getId_usuario(), nombresUsuario);
            events.add(new TimelineEvent(
                    obs.getFecha(),
                    formatFecha(obs.getFecha(), null),
                    "OBSERVACION",
                    "Observación",
                    obs.getComentario(),
                    user,
                    null,
                    "tl-observacion"
            ));
        }

        for (Object o : documentos) {
            if (!(o instanceof Documentos d)) continue;
            LocalDateTime f = parseFlexible(d.getFecha_subida());
            events.add(new TimelineEvent(
                    f,
                    formatFecha(f, d.getFecha_subida()),
                    "DOCUMENTO",
                    "Documento adjunto",
                    d.getNombre() != null ? d.getNombre() : d.getNombre_archivo(),
                    null,
                    (d.getTipo() != null ? d.getTipo() : "") + (d.getTamano() != null ? " · " + d.getTamano() + " bytes" : ""),
                    "tl-documento"
            ));
        }

        for (Object o : firmas) {
            if (!(o instanceof Firmas f)) continue;
            String user = nombreUsuario(f.getIdUsuario(), nombresUsuario);
            events.add(new TimelineEvent(
                    f.getFechaFirma(),
                    formatFecha(f.getFechaFirma(), null),
                    "FIRMA",
                    "Firma · " + (f.getEstado() != null ? f.getEstado() : "pendiente"),
                    f.getObservacion() != null ? f.getObservacion() : (f.getHashDocumento() != null ? "Hash: " + f.getHashDocumento() : "Solicitud de firma"),
                    user,
                    f.getEstado(),
                    "tl-firma"
            ));
        }

        for (Object o : anexos) {
            if (!(o instanceof Anexos a)) continue;
            LocalDateTime f = a.getFechaLimite(); // a menudo solo hay fecha límite; se usa como referencia
            events.add(new TimelineEvent(
                    f,
                    formatFecha(f, null),
                    "ANEXO",
                    "Anexo / soporte",
                    a.getDescripcion(),
                    null,
                    a.getArchivo() != null && a.getArchivo().length() > 60 ? a.getArchivo().substring(0, 57) + "…" : a.getArchivo(),
                    "tl-anexo"
            ));
        }

        for (Object o : reasignaciones) {
            if (!(o instanceof Reasignaciones r)) continue;
            LocalDateTime f = r.getFecha();
            String ant = nombreUsuario(r.getId_usuario_anterior(), nombresUsuario);
            String neu = nombreUsuario(r.getId_usuario_nuevo(), nombresUsuario);
            String depN = r.getId_dependencia_nueva() != null && nombresDep.containsKey(r.getId_dependencia_nueva())
                    ? nombresDep.get(r.getId_dependencia_nueva()) : "—";
            events.add(new TimelineEvent(
                    f,
                    formatFecha(f, null),
                    "REASIGNACION",
                    "Reasignación",
                    "De " + ant + " → " + neu,
                    neu,
                    "Nueva dependencia: " + depN,
                    "tl-reasignacion"
            ));
        }

        Collections.sort(events);
        return events;
    }

    private String nombreUsuario(Long id, Map<Long, String> map) {
        if (id == null) return "—";
        return map.containsKey(id) ? map.get(id) : String.valueOf(id);
    }

    private String formatFecha(LocalDateTime f, String fallback) {
        if (f != null) return f.format(FMT);
        if (fallback != null && !fallback.isBlank()) return fallback;
        return "—";
    }

    private LocalDateTime parseFlexible(String s) {
        if (s == null || s.isBlank()) return null;
        String t = s.trim().replace('T', ' ');
        String[] patterns = {
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd HH:mm",
                "yyyy-MM-dd",
                "dd/MM/yyyy HH:mm:ss",
                "dd/MM/yyyy HH:mm",
                "dd/MM/yyyy"
        };
        for (String p : patterns) {
            try {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern(p);
                if (p.contains("HH")) return LocalDateTime.parse(t.length() > p.length() ? t.substring(0, p.length()) : t, fmt);
                return java.time.LocalDate.parse(t.length() >= 10 ? t.substring(0, 10) : t, fmt).atStartOfDay();
            } catch (Exception ignored) {}
        }
        return null;
    }

    private <T> List<T> safeList(java.util.concurrent.Callable<List<T>> c) {
        try {
            List<T> r = c.call();
            return r != null ? r : List.of();
        } catch (Exception e) {
            return List.of();
        }
    }
}
