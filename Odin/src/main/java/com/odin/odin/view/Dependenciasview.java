package com.odin.odin.view;

import com.odin.odin.model.Dependencias;
import com.odin.odin.repository.DependenciasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class Dependenciasview {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private DependenciasRepository dependenciasRepository;

    @GetMapping("/view/dependencias")
    public String lista(@RequestParam(defaultValue = "0") int page, Model model) {
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<Dependencias> pagina = dependenciasRepository.findAll(pageable);
        model.addAttribute("dependencias", pagina.getContent());
        model.addAttribute("page", pagina);
        model.addAttribute("currentPage", pagina.getNumber());
        model.addAttribute("totalPages", Math.max(pagina.getTotalPages(), 1));
        return "dependencias/dependencias";
    }

    @GetMapping("/view/dependencias/form")
    public String form(Model model) {
        model.addAttribute("dependencias", new Dependencias());
        return "dependencias/dependenciasForm";
    }

    @PostMapping("/view/dependencias/save")
    public String save(@ModelAttribute Dependencias dependencias, RedirectAttributes ra) {
        dependenciasRepository.save(dependencias);
        ra.addFlashAttribute("mensaje", "Dependencia registrada exitosamente");
        return "redirect:/view/dependencias";
    }

    @GetMapping("/view/dependencias/edit/{id}")
    public String edit(@PathVariable long id, Model model) {
        Dependencias dependencias = dependenciasRepository.findById(id).orElse(new Dependencias());
        model.addAttribute("dependencias", dependencias);
        return "dependencias/dependenciasForm";
    }

    @PostMapping("/view/dependencias/delete/{id}")
    public String delete(@PathVariable long id, RedirectAttributes ra) {
        dependenciasRepository.deleteById(id);
        ra.addFlashAttribute("mensaje", "Dependencia eliminada exitosamente");
        return "redirect:/view/dependencias";
    }
}
