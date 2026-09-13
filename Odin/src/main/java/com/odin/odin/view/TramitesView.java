package com.odin.odin.view;

import com.odin.odin.model.Tramites;
import com.odin.odin.repository.TramitesRepository;
import com.odin.odin.repository.RadicadosRepository;
import com.odin.odin.repository.EstadosRepository;
import com.odin.odin.repository.DependenciasRepository;
import com.odin.odin.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/view/tramites")
public class TramitesView {
    @Autowired private TramitesRepository tramitesRepository;
    @Autowired private RadicadosRepository radicadosRepository;
    @Autowired private EstadosRepository estadosRepository;
    @Autowired private DependenciasRepository dependenciasRepository;
    @Autowired private UsuariosRepository usuariosRepository;

    @GetMapping
    public String lista(@RequestParam(required=false) String texto,
                        @RequestParam(required=false) Integer estado,
                        @RequestParam(required=false) Long dependencia,
                        @RequestParam(required=false) Integer tramite,
                        Model model) {
        model.addAttribute("radicados", radicadosRepository.buscar(texto, estado, dependencia, tramite));
        model.addAttribute("tramites", tramitesRepository.findAll());
        model.addAttribute("estados", estadosRepository.findAll());
        model.addAttribute("dependencias", dependenciasRepository.findAll());
        model.addAttribute("usuarios", usuariosRepository.findAll());
        model.addAttribute("textoFiltro", texto);
        model.addAttribute("estadoFiltro", estado);
        model.addAttribute("dependenciaFiltro", dependencia);
        model.addAttribute("tramiteFiltro", tramite);
        model.addAttribute("totalRadicados", radicadosRepository.count());
        model.addAttribute("totalTramites", tramitesRepository.count());
        model.addAttribute("pendientes", radicadosRepository.countPendientes());
        model.addAttribute("enProceso", radicadosRepository.countEnTramite());
        model.addAttribute("finalizados", radicadosRepository.countFinalizados());
        model.addAttribute("vencidos", radicadosRepository.countVencidos(LocalDate.now().format(DateTimeFormatter.ISO_DATE)));
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
        Tramites tramite = tramitesRepository.findById(id).orElseThrow(() -> new RuntimeException("Trámite no encontrado"));
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