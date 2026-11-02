package com.odin.odin.view;

import com.odin.odin.model.Radicados;
import com.odin.odin.model.Usuarios;
import com.odin.odin.repository.DependenciasRepository;
import com.odin.odin.repository.EstadosRepository;
import com.odin.odin.repository.RadicadosRepository;
import com.odin.odin.repository.TramitesRepository;
import com.odin.odin.repository.UsuariosRepository;
import com.odin.odin.service.OdinUserDetails;
import com.odin.odin.util.PlazoUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/view/consulta")
public class ConsultaView {

    private static final int PAGE_SIZE = 15;
    private static final DateTimeFormatter[] FORMATOS_FECHA = {
            DateTimeFormatter.ISO_LOCAL_DATE,
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd")
    };

    private final RadicadosRepository radicadosRepository;
    private final EstadosRepository estadosRepository;
    private final DependenciasRepository dependenciasRepository;
    private final TramitesRepository tramitesRepository;
    private final UsuariosRepository usuariosRepository;

    public ConsultaView(RadicadosRepository radicadosRepository,
                        EstadosRepository estadosRepository,
                        DependenciasRepository dependenciasRepository,
                        TramitesRepository tramitesRepository,
                        UsuariosRepository usuariosRepository) {
        this.radicadosRepository = radicadosRepository;
        this.estadosRepository = estadosRepository;
        this.dependenciasRepository = dependenciasRepository;
        this.tramitesRepository = tramitesRepository;
        this.usuariosRepository = usuariosRepository;
    }

    @GetMapping
    public String consulta(
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) Long estado,
            @RequestParam(required = false) Long dependencia,
            @RequestParam(required = false) Long tramite,
            @RequestParam(required = false) String fechaDesde,
            @RequestParam(required = false) String fechaHasta,
            @RequestParam(required = false, defaultValue = "resultados") String vista,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        if (page < 0) page = 0;
        String vistaActiva = (vista != null && !vista.isBlank()) ? vista.trim().toLowerCase(Locale.ROOT) : "resultados";

        Map<Long, String> nombresEstado = new HashMap<>();
        try {
            estadosRepository.findAll().forEach(e -> {
                if (e.getId_estado() != null) {
                    nombresEstado.put(e.getId_estado().longValue(),
                            e.getNombre() != null ? e.getNombre() : String.valueOf(e.getId_estado()));
                }
            });
        } catch (Exception ignored) { }

        Map<Long, String> nombresDep = new HashMap<>();
        try {
            dependenciasRepository.findAll().forEach(d -> {
                if (d.getId_dependencia() != null) {
                    nombresDep.put(d.getId_dependencia(),
                            d.getNombre() != null ? d.getNombre() : String.valueOf(d.getId_dependencia()));
                }
            });
        } catch (Exception ignored) { }

        Map<Long, String> nombresTramite = new HashMap<>();
        Map<Long, Integer> diasPorTramite = new HashMap<>();
        try {
            tramitesRepository.findAll().forEach(t -> {
                if (t.getIdTramite() != null) {
                    nombresTramite.put(t.getIdTramite(),
                            t.getNombre() != null ? t.getNombre() : String.valueOf(t.getIdTramite()));
                    if (t.getDiasRespuesta() != null) {
                        diasPorTramite.put(t.getIdTramite(), t.getDiasRespuesta());
                    }
                }
            });
        } catch (Exception ignored) { }

        Map<Long, String> nombresUsuario = new HashMap<>();
        try {
            usuariosRepository.findAll().forEach(u -> {
                if (u.getId_usuario() != null) {
                    nombresUsuario.put(u.getId_usuario(),
                            u.getNombre() != null ? u.getNombre() : String.valueOf(u.getId_usuario()));
                }
            });
        } catch (Exception ignored) { }

        // Identificar el usuario autenticado y su dependencia para restringir acceso solo a sus radicados
        Long idUsuarioActual = null;
        Long idDependenciaActual = null;
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                Object principal = auth.getPrincipal();
                if (principal instanceof OdinUserDetails oud && oud.getIdUsuario() != null) {
                    idUsuarioActual = oud.getIdUsuario();
                } else if (auth.getName() != null && !"anonymousUser".equals(auth.getName())) {
                    var uOpt = usuariosRepository.findByLoginFlexible(auth.getName());
                    if (uOpt.isPresent()) {
                        idUsuarioActual = uOpt.get().getId_usuario();
                    }
                }
                if (idUsuarioActual != null) {
                    Usuarios u = usuariosRepository.findById(idUsuarioActual).orElse(null);
                    if (u != null) {
                        idDependenciaActual = u.getId_dependencia();
                    }
                }
            }
        } catch (Exception ignored) { }

        // Cargar radicados desde Supabase y filtrar exclusivamente los enviados a la dependencia y usuario actual
        List<Radicados> todos;
        try {
            todos = new ArrayList<>(radicadosRepository.findAll());
            final Long uid = idUsuarioActual;
            final Long did = idDependenciaActual;
            if (uid != null && did != null) {
                todos = todos.stream()
                        .filter(r -> r != null
                                && did.equals(r.getId_dependencia())
                                && uid.equals(r.getId_usuario()))
                        .collect(Collectors.toList());
            } else if (uid != null) {
                todos = todos.stream()
                        .filter(r -> r != null && uid.equals(r.getId_usuario()))
                        .collect(Collectors.toList());
            }
            todos.sort(Comparator.comparing(
                    (Radicados r) -> r.getId_radicado() != null ? r.getId_radicado() : 0L
            ).reversed());
        } catch (Exception e) {
            todos = Collections.emptyList();
        }

        PlazoUtil.aplicar(todos, diasPorTramite);
        for (Radicados r : todos) {
            if (esEstadoCerradoOTerminal(r, nombresEstado)) {
                r.setDiasRestantes(null);
            }
        }

        String q = (texto != null && !texto.isBlank()) ? texto.trim().toLowerCase(Locale.ROOT) : null;
        LocalDate desde = parseFecha(fechaDesde);
        LocalDate hasta = parseFecha(fechaHasta);

        // 1. Resultados de la consulta (filtros de búsqueda: texto, estado, dependencia, trámite, fechas)
        List<Radicados> listaResultados = todos.stream()
                .filter(r -> coincideFiltros(r, q, estado, dependencia, tramite, desde, hasta, nombresDep, nombresTramite))
                .collect(Collectors.toList());

        // 2. Pendientes: los que están en trámite y todos aquellos que no han recibido ninguna respuesta
        List<Radicados> listaPendientes = todos.stream()
                .filter(r -> esPendienteSinRespuestaOEnTramite(r, nombresEstado))
                .collect(Collectors.toList());

        // 3. Vencidos: radicados activos cuya fecha de vencimiento ya pasó (diasRestantes < 0)
        List<Radicados> listaVencidos = todos.stream()
                .filter(r -> r.getDiasRestantes() != null && r.getDiasRestantes() < 0)
                .collect(Collectors.toList());

        // 4. Próximos a vencer: faltan 3 días o menos para su fecha de vencimiento (0 <= diasRestantes <= 3)
        List<Radicados> listaProximos = todos.stream()
                .filter(r -> r.getDiasRestantes() != null && r.getDiasRestantes() >= 0 && r.getDiasRestantes() <= 3)
                .collect(Collectors.toList());

        // 5. Finalizados: radicados con estado finalizado
        List<Radicados> listaFinalizados = todos.stream()
                .filter(r -> esFinalizado(r, nombresEstado))
                .collect(Collectors.toList());

        // 6. Total: todos los radicados correspondientes al usuario y su dependencia
        List<Radicados> listaTotal = todos;

        // Seleccionar qué lista mostrar en la tabla según la tarjeta/pestaña presionada
        List<Radicados> listaActiva;
        String tituloVista;
        switch (vistaActiva) {
            case "pendientes":
                listaActiva = listaPendientes;
                tituloVista = "Pendientes (en trámite y sin respuesta)";
                break;
            case "vencidos":
                listaActiva = listaVencidos;
                tituloVista = "Radicados vencidos";
                break;
            case "proximos":
                listaActiva = listaProximos;
                tituloVista = "Próximos a vencer (≤ 3 días)";
                break;
            case "finalizados":
                listaActiva = listaFinalizados;
                tituloVista = "Radicados finalizados";
                break;
            case "total":
                listaActiva = listaTotal;
                tituloVista = "Total de radicados";
                break;
            case "resultados":
            default:
                vistaActiva = "resultados";
                listaActiva = listaResultados;
                tituloVista = "Resultados de la consulta";
                break;
        }

        int totalActiva = listaActiva.size();
        int totalPages = Math.max(1, (int) Math.ceil(totalActiva / (double) PAGE_SIZE));
        if (page >= totalPages) {
            page = Math.max(0, totalPages - 1);
        }
        int from = Math.min(page * PAGE_SIZE, totalActiva);
        int to = Math.min(from + PAGE_SIZE, totalActiva);
        List<Radicados> slice = listaActiva.subList(from, to);

        model.addAttribute("radicados", slice);
        model.addAttribute("estados", estadosRepository.findAll());
        model.addAttribute("dependencias", dependenciasRepository.findAll());
        model.addAttribute("tramites", tramitesRepository.findAll());
        model.addAttribute("nombresEstado", nombresEstado);
        model.addAttribute("nombresDep", nombresDep);
        model.addAttribute("nombresTramite", nombresTramite);
        model.addAttribute("nombresUsuario", nombresUsuario);

        model.addAttribute("textoFiltro", texto);
        model.addAttribute("estadoFiltro", estado);
        model.addAttribute("dependenciaFiltro", dependencia);
        model.addAttribute("tramiteFiltro", tramite);
        model.addAttribute("fechaDesdeFiltro", fechaDesde);
        model.addAttribute("fechaHastaFiltro", fechaHasta);
        model.addAttribute("vistaActiva", vistaActiva);
        model.addAttribute("tituloVista", tituloVista);

        // La tarjeta "Resultados" se adapta dinámicamente a la pestaña seleccionada (totalActiva)
        model.addAttribute("total", totalActiva);
        model.addAttribute("pendientes", listaPendientes.size());
        model.addAttribute("vencidos", listaVencidos.size());
        model.addAttribute("proximos", listaProximos.size());
        model.addAttribute("finalizados", listaFinalizados.size());
        model.addAttribute("totalGeneral", listaTotal.size());

        model.addAttribute("totalMostrado", totalActiva);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "consulta/consulta";
    }

    private boolean coincideFiltros(Radicados r,
                                    String q,
                                    Long estado,
                                    Long dependencia,
                                    Long tramite,
                                    LocalDate desde,
                                    LocalDate hasta,
                                    Map<Long, String> nombresDep,
                                    Map<Long, String> nombresTramite) {
        if (r == null) return false;

        if (estado != null && !estado.equals(r.getId_estado())) {
            return false;
        }
        if (dependencia != null && !dependencia.equals(r.getId_dependencia())) {
            return false;
        }
        if (tramite != null && !tramite.equals(r.getId_tramite())) {
            return false;
        }

        if (desde != null || hasta != null) {
            LocalDate fechaRad = parseFecha(r.getFecha_radicado());
            if (fechaRad == null) return false;
            if (desde != null && fechaRad.isBefore(desde)) return false;
            if (hasta != null && fechaRad.isAfter(hasta)) return false;
        }

        if (q != null && !q.isEmpty()) {
            String num = r.getNumero_radicado() != null ? r.getNumero_radicado().toLowerCase(Locale.ROOT) : "";
            String asunto = r.getAsunto() != null ? r.getAsunto().toLowerCase(Locale.ROOT) : "";
            String rem = r.getRemitente() != null ? r.getRemitente().toLowerCase(Locale.ROOT) : "";
            String depName = (r.getId_dependencia() != null && nombresDep.containsKey(r.getId_dependencia()))
                    ? nombresDep.get(r.getId_dependencia()).toLowerCase(Locale.ROOT) : "";
            String tramName = (r.getId_tramite() != null && nombresTramite.containsKey(r.getId_tramite()))
                    ? nombresTramite.get(r.getId_tramite()).toLowerCase(Locale.ROOT) : "";

            if (!num.contains(q) && !asunto.contains(q) && !rem.contains(q)
                    && !depName.contains(q) && !tramName.contains(q)) {
                return false;
            }
        }
        return true;
    }

    private boolean esFinalizado(Radicados r, Map<Long, String> nombresEstado) {
        if (r == null) return false;
        Long idEst = r.getId_estado();
        if (idEst != null && idEst == 7L) return true;
        String nombre = obtenerNombreEstado(r, nombresEstado);
        return nombre.contains("finaliz");
    }

    private boolean esEstadoCerradoOTerminal(Radicados r, Map<Long, String> nombresEstado) {
        if (r == null) return true;
        if (r.getFecha_cierre() != null) return true;
        Long idEst = r.getId_estado();
        // 3 = devuelto, 4 = rechazado, 5 = archivado, 7 = finalizado
        if (idEst != null && (idEst == 3L || idEst == 4L || idEst == 5L || idEst == 7L)) {
            return true;
        }
        String nombre = obtenerNombreEstado(r, nombresEstado);
        return nombre.contains("finaliz")
                || nombre.contains("archiv")
                || nombre.contains("cerrad")
                || nombre.contains("rechaz")
                || nombre.contains("devuelt")
                || nombre.contains("anul");
    }

    private boolean esPendienteSinRespuestaOEnTramite(Radicados r, Map<Long, String> nombresEstado) {
        if (r == null) return false;
        if (esEstadoCerradoOTerminal(r, nombresEstado)) {
            return false;
        }
        Long idEst = r.getId_estado();
        // 1 = radicado (sin respuesta), 2 = en tramite, 6 = trasladado (sin respuesta aún)
        if (idEst != null && (idEst == 1L || idEst == 2L || idEst == 6L)) {
            return true;
        }
        String nombre = obtenerNombreEstado(r, nombresEstado);
        return nombre.contains("radicad")
                || nombre.contains("tramite")
                || nombre.contains("trámite")
                || nombre.contains("pendient")
                || nombre.contains("proceso")
                || nombre.contains("traslad");
    }

    private String obtenerNombreEstado(Radicados r, Map<Long, String> nombresEstado) {
        if (r.getId_estado() != null && nombresEstado != null && nombresEstado.containsKey(r.getId_estado())) {
            String n = nombresEstado.get(r.getId_estado());
            if (n != null) return n.toLowerCase(Locale.ROOT);
        }
        try {
            if (r.getEstado() != null && r.getEstado().getNombre() != null) {
                return r.getEstado().getNombre().toLowerCase(Locale.ROOT);
            }
        } catch (Exception ignored) { }
        return "";
    }

    private LocalDate parseFecha(String texto) {
        if (texto == null || texto.isBlank()) return null;
        String t = texto.trim();
        if (t.length() >= 10) t = t.substring(0, 10);
        for (DateTimeFormatter fmt : FORMATOS_FECHA) {
            try {
                return LocalDate.parse(t, fmt);
            } catch (DateTimeParseException ignored) { }
        }
        return null;
    }
}
