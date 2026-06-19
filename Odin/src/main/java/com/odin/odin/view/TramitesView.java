package com.odin.odin.view;

import com.odin.odin.model.Tramites;
import com.odin.odin.model.Radicados;
import com.odin.odin.model.Estados;
import com.odin.odin.model.Dependencias;
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

import java.util.List;

@Controller
@RequestMapping("/view/tramites")  // ← Thymeleaf, no API
public class TramitesView {

    @Autowired
    private TramitesRepository tramitesRepository;

    @Autowired
    private RadicadosRepository radicadosRepository;

    @Autowired
    private EstadosRepository estadosRepository;

    @Autowired
    private DependenciasRepository dependenciasRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    // ============================================================
    // LISTA - Vista: tramites/ges_tramites.html
    // ============================================================
    @GetMapping
    public String lista(Model model) {
        // Datos para la tabla de trámites
        model.addAttribute("tramites", tramitesRepository.findAll());

        // Datos para la tabla de radicados (gestión de trámites)
        model.addAttribute("radicados", radicadosRepository.findAll());

        // Catálogos para filtros
        model.addAttribute("estados", estadosRepository.findAll());
        model.addAttribute("dependencias", dependenciasRepository.findAll());
        model.addAttribute("usuarios", usuariosRepository.findAll());

        // Estadísticas (KPIs)
        model.addAttribute("totalRadicados", radicadosRepository.count());
        model.addAttribute("pendientes", radicadosRepository.countPendientes());
        model.addAttribute("enProceso", radicadosRepository.countEnTramite());
        model.addAttribute("finalizados", radicadosRepository.countFinalizados());
        model.addAttribute("vencidos", radicadosRepository.countVencidos());

        return "tramites/ges_tramites";
    }

    // ============================================================
    // FORMULARIO NUEVO - Vista: tramites/tramitesForm.html
    // ============================================================
    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("tramites", new Tramites());
        model.addAttribute("estados", estadosRepository.findAll());
        model.addAttribute("dependencias", dependenciasRepository.findAll());
        return "tramites/tramitesForm";
    }

    // ============================================================
    // EDITAR - Vista: tramites/tramitesForm.html
    // ============================================================
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Tramites tramite = tramitesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trámite no encontrado"));
        model.addAttribute("tramites", tramite);
        model.addAttribute("estados", estadosRepository.findAll());
        model.addAttribute("dependencias", dependenciasRepository.findAll());
        return "tramites/tramitesForm";
    }

    // ============================================================
    // GUARDAR (POST)
    // ============================================================
    @PostMapping("/save")
    public String save(@ModelAttribute Tramites tramites, RedirectAttributes ra) {
        tramitesRepository.save(tramites);
        ra.addFlashAttribute("mensaje", "Trámite registrado con éxito");
        return "redirect:/view/tramites";
    }

    // ============================================================
    // ELIMINAR (POST)
    // ============================================================
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        tramitesRepository.deleteById(id);
        ra.addFlashAttribute("mensaje", "Trámite eliminado con éxito");
        return "redirect:/view/tramites";
    }

    // ============================================================
    // API INTERNA (SOLO PARA LA VISTA)
    // Nota: Para API pública usar TramitesController (/api/tramites)
    // ============================================================
    @GetMapping("/api/all")
    @ResponseBody
    public List<Tramites> getAllForAjax() {
        return tramitesRepository.findAll();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public Tramites getByIdForAjax(@PathVariable Long id) {
        return tramitesRepository.findById(id).orElse(null);
    }
}