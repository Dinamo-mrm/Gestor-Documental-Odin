package com.odin.odin.view;

import com.odin.odin.repository.PlantillaRepository;
import com.odin.odin.repository.RadicadosRepository;
import com.odin.odin.repository.ReasignacionesRepository;
import com.odin.odin.repository.TramitesRepository;
import com.odin.odin.repository.UsuariosRepository;

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
    private PlantillaRepository plantillaRepository;

    @Autowired
    private RadicadosRepository radicadosRepository;

    @Autowired
    private ReasignacionesRepository reasignacionesRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        // Totales para las tarjetas del dashboard
        model.addAttribute("totalTramites", tramitesRepository.count());
        model.addAttribute("totalPlantillas", plantillaRepository.count());
        model.addAttribute("totalRadicados", radicadosRepository.count());
        model.addAttribute("totalReasignaciones", reasignacionesRepository.count());
        model.addAttribute("totalUsuarios", usuariosRepository.count());

        return "dashboard/dashboard";
    }
}