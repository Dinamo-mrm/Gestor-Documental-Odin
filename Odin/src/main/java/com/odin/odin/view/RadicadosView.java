package com.odin.odin.view;

import com.odin.odin.model.Radicados;
import com.odin.odin.repository.DependenciasRepository;
import com.odin.odin.repository.DocumentosRepository;
import com.odin.odin.repository.EstadosRepository;
import com.odin.odin.repository.RadicadosRepository;
import com.odin.odin.repository.SeriesRepository;
import com.odin.odin.repository.SubseriesRepository;
import com.odin.odin.repository.TramitesRepository;
import com.odin.odin.repository.UsuariosRepository;
import com.odin.odin.service.RadicacionService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RadicadosView {

    private final RadicadosRepository radicadosRepository;
    private final DependenciasRepository dependenciasRepository;
    private final SeriesRepository seriesRepository;
    private final SubseriesRepository subseriesRepository;
    private final TramitesRepository tramitesRepository;
    private final EstadosRepository estadosRepository;
    private final UsuariosRepository usuariosRepository;
    private final DocumentosRepository documentosRepository;
    private final RadicacionService radicacionService;

    public RadicadosView(
            RadicadosRepository radicadosRepository,
            DependenciasRepository dependenciasRepository,
            SeriesRepository seriesRepository,
            SubseriesRepository subseriesRepository,
            TramitesRepository tramitesRepository,
            EstadosRepository estadosRepository,
            UsuariosRepository usuariosRepository,
            DocumentosRepository documentosRepository,
            RadicacionService radicacionService) {
        this.radicadosRepository = radicadosRepository;
        this.dependenciasRepository = dependenciasRepository;
        this.seriesRepository = seriesRepository;
        this.subseriesRepository = subseriesRepository;
        this.tramitesRepository = tramitesRepository;
        this.estadosRepository = estadosRepository;
        this.usuariosRepository = usuariosRepository;
        this.documentosRepository = documentosRepository;
        this.radicacionService = radicacionService;
    }

    @GetMapping({"/view/radicados", "/view/radicados/documental"})
    public String documental(Model model) {
        if (!model.containsAttribute("radicado")) {
            model.addAttribute("radicado", new Radicados());
        }
        cargarCatalogos(model);
        return "radicados/radicacion_Documental";
    }

    private void cargarCatalogos(Model model) {
        model.addAttribute("dependencias", dependenciasRepository.findAll());
        model.addAttribute("series", seriesRepository.findAll());
        model.addAttribute("subseries", subseriesRepository.findAll());
        model.addAttribute("tramites", tramitesRepository.findAll());
        model.addAttribute("estados", estadosRepository.findAll());
        model.addAttribute("usuarios", usuariosRepository.findAll());
    }

    @PostMapping({"/view/radicados/save", "/view/radicados/documental/save"})
    public String save(
            @Valid @ModelAttribute("radicado") Radicados radicado,
            BindingResult bindingResult,
            RedirectAttributes ra) {

        if (bindingResult.hasErrors()) {
            ra.addFlashAttribute("error", "Revise los campos obligatorios de la radicación");
            return "redirect:/view/radicados/documental";
        }

        try {
            RadicacionService.ResultadoRadicacion resultado =
                    radicacionService.guardar(radicado, radicado.getArchivos());

            ra.addFlashAttribute("success",
                    "Radicación documental registrada correctamente: "
                            + resultado.radicado().getNumero_radicado());
            ra.addFlashAttribute("mensaje",
                    "Radicación documental registrada correctamente");
            ra.addFlashAttribute("savedRadicado", resultado.radicado());
            ra.addFlashAttribute("archivos", resultado.archivosGuardados());

        } catch (IllegalArgumentException | IllegalStateException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/view/radicados/documental";
    }

    @GetMapping("/view/radicados/edit/{id}")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Radicados radicado = radicadosRepository.findById(id).orElse(null);
        if (radicado == null) {
            ra.addFlashAttribute("error", "El radicado solicitado no existe");
            return "redirect:/view/radicados/documental";
        }
        model.addAttribute("radicado", radicado);
        cargarCatalogos(model);
        return "radicados/radicacion_Documental";
    }

    @GetMapping("/view/radicados/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Radicados radicado = radicadosRepository.findById(id).orElse(null);
        if (radicado == null) {
            ra.addFlashAttribute("error", "El radicado solicitado no existe");
            return "redirect:/view/tramites";
        }
        model.addAttribute("radicado", radicado);
        model.addAttribute("documentos", documentosRepository.buscarPorRadicado(id));
        return "radicados/detalle";
    }

    @PostMapping("/view/radicados/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        if (!radicadosRepository.existsById(id)) {
            ra.addFlashAttribute("error", "El radicado solicitado no existe");
            return "redirect:/view/radicados/documental";
        }

        // Los registros documentales no se eliminan físicamente desde la UI.
        // Se mantiene trazabilidad y se evita borrar evidencia accidentalmente.
        ra.addFlashAttribute("error",
                "Los radicados no se eliminan físicamente. Use el flujo de anulación/archivo.");
        return "redirect:/view/radicados/documental";
    }
}
