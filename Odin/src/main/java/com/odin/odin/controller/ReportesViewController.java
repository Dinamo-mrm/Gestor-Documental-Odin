package com.odin.odin.controller;

import com.odin.odin.repository.RadicadosRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view/reportes")
public class ReportesViewController {

    private final RadicadosRepository radicadosRepository;

    public ReportesViewController(RadicadosRepository radicadosRepository) {
        this.radicadosRepository = radicadosRepository;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("title", "Reportes y Estadísticas");
        try {
            model.addAttribute("totalRadicados", radicadosRepository.count());
            model.addAttribute("pendientes", radicadosRepository.countPendientes());
            model.addAttribute("enProceso", radicadosRepository.countEnTramite());
            model.addAttribute("finalizados", radicadosRepository.countFinalizados());
            model.addAttribute("vencidos", radicadosRepository.countVencidos());
        } catch (Exception e) {
            model.addAttribute("totalRadicados", 0);
            model.addAttribute("pendientes", 0);
            model.addAttribute("enProceso", 0);
            model.addAttribute("finalizados", 0);
            model.addAttribute("vencidos", 0);
        }
        return "reportes/reportes";
    }
}
