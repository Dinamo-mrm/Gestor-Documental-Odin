package com.odin.odin.controller;

import com.odin.odin.dto.DashboardResumen;
import com.odin.odin.repository.RadicadosRepository;
import com.odin.odin.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private RadicadosRepository radicadosRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute(
                "totalRadicados",
                dashboardService.totalRadicados());

        model.addAttribute(
                "pendientes",
                dashboardService.radicadosPendientes());

        model.addAttribute(
                "vencidos",
                dashboardService.radicadosVencidos());

        model.addAttribute(
                "usuariosActivos",
                dashboardService.usuariosActivos());

        return "dashboard";
    }
}