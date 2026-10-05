package com.odin.odin.view;

import com.odin.odin.model.Radicados;
import com.odin.odin.model.Tramites;
import com.odin.odin.repository.DependenciasRepository;
import com.odin.odin.repository.EstadosRepository;
import com.odin.odin.repository.RadicadosRepository;
import com.odin.odin.repository.TramitesRepository;
import com.odin.odin.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.odin.odin.util.PlazoUtil;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/view/tramites")
public class TramitesView {

    private static final int PAGE_SIZE = 10;

    @Autowired private TramitesRepository tramitesRepository;
    @Autowired private RadicadosRepository radicadosRepository;
    @Autowired private EstadosRepository estadosRepository;
    @Autowired private DependenciasRepository dependenciasRepository;
    @Autowired private UsuariosRepository usuariosRepository;

    @GetMapping
    public String lista(@RequestParam(required = false) String texto,
                        @RequestParam(required = false) Long estado,
                        @RequestParam(required = false) Long dependencia,
                        @RequestParam(required = false) Long tramite,
                        @RequestParam(required = false) String vencimiento,
                        @RequestParam(defaultValue = "0") int page,
                        Model model) {

        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<Radicados> pagina;

        if ("vencidos".equalsIgnoreCase(vencimiento)) {
            pagina = radicadosRepository.findVencidos(pageable);
        } else if ("proximos".equalsIgnoreCase(vencimiento)) {
            // lista corta; envolver en página simple
            var lista = radicadosRepository.findProximosAVencer(3);
            int from = Math.min(page * PAGE_SIZE, lista.size());
            int to = Math.min(from + PAGE_SIZE, lista.size());
            pagina = new org.springframework.data.domain.PageImpl<>(
                    lista.subList(from, to), pageable, lista.size());
        } else {
            pagina = radicadosRepository.buscar(texto, estado, dependencia, tramite, pageable);
        }

        var contenido = pagina.getContent();
        Map<Long, Integer> diasPorTramite = new HashMap<>();
        tramitesRepository.findAll().forEach(tr -> {
            if (tr.getIdTramite() != null && tr.getDiasRespuesta() != null)
                diasPorTramite.put(tr.getIdTramite(), tr.getDiasRespuesta());
        });
        PlazoUtil.aplicar(contenido, diasPorTramite);
        model.addAttribute("radicados", contenido);
        model.addAttribute("page", pagina);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pagina.getTotalPages());
        model.addAttribute("totalElements", pagina.getTotalElements());

        model.addAttribute("listaTramites", tramitesRepository.findAll());
        model.addAttribute("tramites", tramitesRepository.findAll());
        model.addAttribute("estados", estadosRepository.findAll());
        model.addAttribute("dependencias", dependenciasRepository.findAll());
        model.addAttribute("usuarios", usuariosRepository.findAll());

        model.addAttribute("textoFiltro", texto);
        model.addAttribute("estadoFiltro", estado);
        model.addAttribute("dependenciaFiltro", dependencia);
        model.addAttribute("tramiteFiltro", tramite);
        model.addAttribute("vencimientoFiltro", vencimiento);

        // Solo conteos (baratos) — no cargar listas completas de vencidos
        model.addAttribute("totalRadicados", radicadosRepository.count());
        model.addAttribute("totalTramites", tramitesRepository.count());
        model.addAttribute("pendientes", radicadosRepository.countPendientes());
        model.addAttribute("enProceso", radicadosRepository.countEnTramite());
        model.addAttribute("finalizados", radicadosRepository.countFinalizados());
        model.addAttribute("vencidos", radicadosRepository.countVencidos());
        
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
        model.addAttribute("nombresEstado", nombresEstado);
        model.addAttribute("nombresDep", nombresDep);
        model.addAttribute("nombresTramite", nombresTramite);
        model.addAttribute("nombresUsuario", nombresUsuario);

        model.addAttribute("diasAlertaVencimiento", 3);

        return "tramites/ges_tramites";
    }

    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("tramites", new Tramites());
        model.addAttribute("estados", estadosRepository.findAll());
        model.addAttribute("dependencias", dependenciasRepository.findAll());
        return "tramites/tramitesForm";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Tramites tramite = tramitesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trámite no encontrado"));
        model.addAttribute("tramites", tramite);
        model.addAttribute("estados", estadosRepository.findAll());
        model.addAttribute("dependencias", dependenciasRepository.findAll());
        return "tramites/tramitesForm";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Tramites tramites, RedirectAttributes ra) {
        tramitesRepository.save(tramites);
        ra.addFlashAttribute("mensaje", "Trámite registrado con éxito");
        return "redirect:/view/tramites";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        tramitesRepository.deleteById(id);
        ra.addFlashAttribute("mensaje", "Trámite eliminado con éxito");
        return "redirect:/view/tramites";
    }
}
