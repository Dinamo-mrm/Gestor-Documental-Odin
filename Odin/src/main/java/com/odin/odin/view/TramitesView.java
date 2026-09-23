package com.odin.odin.view;

import com.odin.odin.model.Tramites;
import com.odin.odin.repository.DependenciasRepository;
import com.odin.odin.repository.EstadosRepository;
import com.odin.odin.repository.RadicadosRepository;
import com.odin.odin.repository.TramitesRepository;
import com.odin.odin.repository.UsuariosRepository;
import com.odin.odin.service.TramiteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/view/tramites")
public class TramitesView {

    private final TramitesRepository tramitesRepository;
    private final RadicadosRepository radicadosRepository;
    private final EstadosRepository estadosRepository;
    private final DependenciasRepository dependenciasRepository;
    private final UsuariosRepository usuariosRepository;
    private final TramiteService tramiteService;

    public TramitesView(
            TramitesRepository tramitesRepository,
            RadicadosRepository radicadosRepository,
            EstadosRepository estadosRepository,
            DependenciasRepository dependenciasRepository,
            UsuariosRepository usuariosRepository,
            TramiteService tramiteService) {
        this.tramitesRepository = tramitesRepository;
        this.radicadosRepository = radicadosRepository;
        this.estadosRepository = estadosRepository;
        this.dependenciasRepository = dependenciasRepository;
        this.usuariosRepository = usuariosRepository;
        this.tramiteService = tramiteService;
    }

    @GetMapping
    public String lista(
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) Integer estado,
            @RequestParam(required = false) Long dependencia,
            @RequestParam(required = false) Long tramite,
            @RequestParam(required = false) String vencimiento,
            Model model) {

        if ("vencidos".equalsIgnoreCase(vencimiento)) {
            model.addAttribute("radicados", radicadosRepository.findVencidos());
        } else if ("proximos".equalsIgnoreCase(vencimiento)) {
            model.addAttribute("radicados", radicadosRepository.findProximosAVencer(3));
        } else {
            model.addAttribute("radicados",
                    radicadosRepository.buscar(texto, estado, dependencia, tramite));
        }

        model.addAttribute("tramites", tramitesRepository.findAll());
        model.addAttribute("estados", estadosRepository.findAll());
        model.addAttribute("dependencias", dependenciasRepository.findAll());
        model.addAttribute("usuarios", usuariosRepository.findAll());

        model.addAttribute("textoFiltro", texto);
        model.addAttribute("estadoFiltro", estado);
        model.addAttribute("dependenciaFiltro", dependencia);
        model.addAttribute("tramiteFiltro", tramite);
        model.addAttribute("vencimientoFiltro", vencimiento);

        model.addAttribute("totalRadicados", radicadosRepository.count());
        model.addAttribute("totalTramites", tramitesRepository.count());
        model.addAttribute("tramitesActivos", tramitesRepository.findByActivoTrueOrderByNombreAsc().size());
        model.addAttribute("pendientes", radicadosRepository.countPendientes());
        model.addAttribute("enProceso", radicadosRepository.countEnTramite());
        model.addAttribute("finalizados", radicadosRepository.countFinalizados());
        model.addAttribute("vencidos", radicadosRepository.countVencidos());
        model.addAttribute("radicadosVencidos", radicadosRepository.findVencidos());
        model.addAttribute("proximosAVencer", radicadosRepository.findProximosAVencer(3));
        model.addAttribute("diasAlertaVencimiento", 3);

        return "tramites/ges_tramites";
    }

    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("tramites", new Tramites());
        cargarCatalogos(model);
        return "tramites/tramitesForm";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Tramites tramite = tramitesRepository.findById(id).orElse(null);
        if (tramite == null) {
            ra.addFlashAttribute("error", "El trámite solicitado no existe");
            return "redirect:/view/tramites";
        }
        model.addAttribute("tramites", tramite);
        cargarCatalogos(model);
        return "tramites/tramitesForm";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("tramites") Tramites tramites,
                       RedirectAttributes ra) {
        try {
            boolean nuevo = tramites.getIdTramite() == null;
            Tramites guardado = tramiteService.guardar(tramites);
            ra.addFlashAttribute("mensaje",
                    nuevo
                            ? "Trámite registrado correctamente"
                            : "Trámite actualizado correctamente");
            ra.addFlashAttribute("tramiteGuardado", guardado.getIdTramite());
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/view/tramites";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            tramiteService.desactivar(id);
            ra.addFlashAttribute("mensaje",
                    "Trámite desactivado correctamente. Los registros históricos se conservan.");
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/view/tramites";
    }

    private void cargarCatalogos(Model model) {
        model.addAttribute("estados", estadosRepository.findAll());
        model.addAttribute("dependencias", dependenciasRepository.findAll());
    }
}
