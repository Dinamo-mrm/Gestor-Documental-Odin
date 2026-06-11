package com.odin.odin.view;

import com.odin.odin.model.Tramites;
import com.odin.odin.repository.TramitesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/view/tramites")
public class TramitesView {

    @Autowired
    private TramitesRepository tramitesRepository;

    @GetMapping
    public String lista(Model model) {

        model.addAttribute("tramites",
                tramitesRepository.findAll());

        return "tramites/tramites";
    }

    @GetMapping("/form")
    public String form(Model model) {

        model.addAttribute("tramites",
                new Tramites());

        return "tramites/tramitesForm";
    }

    @PostMapping("/save")
    public String save(
            @ModelAttribute Tramites tramites,
            RedirectAttributes ra) {

        tramitesRepository.save(tramites);

        ra.addFlashAttribute(
                "mensaje",
                "Trámite registrado con éxito");

        return "redirect:/view/tramites";
    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            Model model) {

        Tramites tramite = tramitesRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Trámite no encontrado"));

        model.addAttribute("tramites", tramite);

        return "tramites/tramitesForm";
    }

    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable Long id,
            RedirectAttributes ra) {

        Tramites tramite = tramitesRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Trámite no encontrado"));

        tramitesRepository.delete(tramite);

        ra.addFlashAttribute(
                "mensaje",
                "Trámite eliminado con éxito");

        return "redirect:/view/tramites";
    }
}