package com.odin.odin.view;

import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Page;

import com.odin.odin.model.Reasignaciones;
import com.odin.odin.repository.ReasignacionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReasignacionesView {
    private static final int PAGE_SIZE = 10;


    @Autowired
    private ReasignacionesRepository reasignacionesRepository;

    @GetMapping("/view/reasignaciones")
    public String lista(@RequestParam(defaultValue = "0") int page, Model model) {
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<?> pagina = reasignacionesRepository.findAll(pageable);
        model.addAttribute("reasignaciones", pagina.getContent());
        model.addAttribute("page", pagina);
        model.addAttribute("currentPage", pagina.getNumber());
        model.addAttribute("totalPages", Math.max(pagina.getTotalPages(), 1));
        return "reasignaciones/reasignaciones";
    }

    @GetMapping("/view/reasignaciones/form")
    public String form(Model model) {
        model.addAttribute("reasignaciones", new Reasignaciones());
        return "reasignaciones/reasignacionesForm";
    }

    @PostMapping("/view/reasignaciones/save")
    public String save(@ModelAttribute Reasignaciones reasignaciones, RedirectAttributes ra) {
        reasignacionesRepository.save(reasignaciones);
        ra.addFlashAttribute("success", "Reasignación registrada con éxito");
        return "redirect:/view/reasignaciones";
    }

    @GetMapping("/view/reasignaciones/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Reasignaciones reasignaciones = reasignacionesRepository.findById(id).orElse(null);
        model.addAttribute("reasignaciones", reasignaciones);
        return "reasignaciones/reasignacionesForm";
    }

    @PostMapping("/view/reasignaciones/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        reasignacionesRepository.deleteById(id);
        ra.addFlashAttribute("success", "Reasignación eliminada con éxito");
        return "redirect:/view/reasignaciones";
    }
}