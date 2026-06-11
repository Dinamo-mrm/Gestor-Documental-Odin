package com.odin.odin.view;

import com.odin.odin.repository.PlantillaRepository;
import com.odin.odin.repository.RadicadosRepository;
import com.odin.odin.repository.ReasignacionesRepository;
import com.odin.odin.repository.TramitesRepository;
import com.odin.odin.repository.UsuariosRepository;

import com.odin.odin.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
public class DashboardView {

    @Autowired
    private TramitesRepository tramitesRepository;

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private PlantillaRepository plantillaRepository;

    @Autowired
    private RadicadosRepository radicadosRepository;

    @Autowired
    private ReasignacionesRepository reasignacionesRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute(
                "pendientes",
                dashboardService.pendientes());

        model.addAttribute(
                "finalizados",
                dashboardService.finalizados());

        model.addAttribute(
                "problemas",
                dashboardService.problemas());

        model.addAttribute(
                "vencidos",
                dashboardService.vencidos());

        model.addAttribute(
                "ultimosRadicados",
                radicadosRepository
                        .findTop10ByOrderByIdRadicadoDesc());

        return "dashboard/dashboard";
    }
}