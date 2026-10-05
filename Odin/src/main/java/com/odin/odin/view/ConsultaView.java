package com.odin.odin.view;

import com.odin.odin.model.Radicados;
import com.odin.odin.repository.DependenciasRepository;
import com.odin.odin.repository.EstadosRepository;
import com.odin.odin.repository.RadicadosRepository;
import com.odin.odin.repository.TramitesRepository;
import com.odin.odin.repository.UsuariosRepository;
import com.odin.odin.util.PlazoUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/view/consulta")
public class ConsultaView {

    private static final int PAGE_SIZE = 10;

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
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        if (page < 0) page = 0;
        String q = (texto != null && !texto.isBlank()) ? texto.trim() : null;
        List<Radicados> lista;
        try {
            if (q != null) {
                lista = radicadosRepository.buscarTextoCompleto(q, estado, dependencia, tramite);
                if (lista == null || lista.isEmpty()) {
                    lista = radicadosRepository.buscar(q, estado, dependencia, tramite);
                }
            } else {
                lista = radicadosRepository.buscar(null, estado, dependencia, tramite);
            }
        } catch (Exception e) {
            lista = Collections.emptyList();
        }
        if (lista == null) lista = Collections.emptyList();

        Map<Long, Integer> diasPorTramite = new HashMap<>();
        try {
            tramitesRepository.findAll().forEach(tr -> {
                if (tr.getIdTramite() != null && tr.getDiasRespuesta() != null) {
                    diasPorTramite.put(tr.getIdTramite(), tr.getDiasRespuesta());
                }
            });
        } catch (Exception ignored) { }
        PlazoUtil.aplicar(lista, diasPorTramite);

        int from = Math.min(page * PAGE_SIZE, lista.size());
        int to = Math.min(from + PAGE_SIZE, lista.size());
        List<Radicados> slice = lista.subList(from, to);
        int totalPages = Math.max(1, (int) Math.ceil(lista.size() / (double) PAGE_SIZE));

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
        tramitesRepository.findAll().forEach(t -> {
            if (t.getIdTramite() != null)
                nombresTramite.put(t.getIdTramite(), t.getNombre() != null ? t.getNombre() : String.valueOf(t.getIdTramite()));
        });
        Map<Long, String> nombresUsuario = new HashMap<>();
        usuariosRepository.findAll().forEach(u -> {
            if (u.getId_usuario() != null)
                nombresUsuario.put(u.getId_usuario(), u.getNombre() != null ? u.getNombre() : String.valueOf(u.getId_usuario()));
        });

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
        model.addAttribute("total", lista.size());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("vencidos", safeCount(() -> radicadosRepository.countVencidos()));
        model.addAttribute("pendientes", safeCount(() -> radicadosRepository.countPendientes()));
        return "consulta/consulta";
    }

    private Long safeCount(java.util.concurrent.Callable<Long> c) {
        try {
            Long v = c.call();
            return v == null ? 0L : v;
        } catch (Exception e) {
            return 0L;
        }
    }
}
