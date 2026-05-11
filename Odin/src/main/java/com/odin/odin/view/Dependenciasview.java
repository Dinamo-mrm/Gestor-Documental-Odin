package com.odin.odin.view;

import com.odin.odin.model.Dependencias;
import com.odin.odin.repository.DependenciasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
public class Dependenciasview
{
    @Autowired
    private DependenciasRepository dependenciasRepository;

    @GetMapping("/view/dependencias")
    public String lista(Model model)
    {
        model.addAttribute("dependencias", dependenciasRepository.findAll());
        return "dependencias/dependencias";
    }

    @GetMapping("/view/dependencias/form")
    public String form(Model model)
    {
        model.addAttribute("dependencias", new Dependencias());
        return "dependencias/dependenciasForm";
    }

    @PostMapping("/view/dependencias/save")
    public String save(@ModelAttribute Dependencias dependencias, RedirectAttributes ra)
    {
        dependenciasRepository.save(dependencias);
        ra.addFlashAttribute("mensaje", "Dependencia registrado exitosamente");
        return "redirect:/view/dependencias";
    }

    @GetMapping("/view/dependencias/edit/{id}")
    public String edit(@PathVariable long id, Model model)
    {
        Dependencias dependencias = dependenciasRepository.findById(id).orElse(null);
        model.addAttribute("dependencias", dependencias);
        return "dependencias/dependenciasForm";
    }

    @PostMapping("/view/dependencias/delete/{id}")
    public String delete(@PathVariable long id, RedirectAttributes ra)
    {
        dependenciasRepository.deleteById(id);
        ra.addFlashAttribute("mensaje", "dependencia eliminado exitosamente");
        return "redirect:/view/dependencias";
    }


}