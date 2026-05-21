package com.odin.odin.view;

import com.odin.odin.model.Plantilla;
import com.odin.odin.repository.PlantillaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PlantillaView
{
    @Autowired
    private PlantillaRepository plantillaRepository;

    // LISTA
    @GetMapping("/view/plantilla")
    public String lista(Model model)
    {
        model.addAttribute("plantilla", plantillaRepository.findAll());

        return "Plantilla/Plantilla";
    }

    // FORMULARIO
    @GetMapping("/view/bitacoras/form")
    public String form(Model model)
    {
        model.addAttribute("plantilla", new Plantilla());

        return "Plantilla/PlantillaForm";
    }

    // GUARDAR
    @PostMapping("/view/bitacoras/save")
    public String save(@ModelAttribute Plantilla bitacora,
                       RedirectAttributes ra)
    {
        plantillaRepository.save(bitacora);

        ra.addFlashAttribute("mensaje",
                "Plantilla registrada con éxito");

        return "redirect:/view/bitacoras";
    }

    // EDITAR
    @GetMapping("/view/bitacoras/edit/{id}")
    public String edit(@PathVariable Long id,
                       Model model)
    {
        Plantilla bitacora =
                plantillaRepository.findById(id).orElse(null);

        model.addAttribute("bitacora", bitacora);

        return "Plantilla/BitacorasForm";
    }

    // ELIMINAR
    @PostMapping("/view/bitacoras/delete/{id}")
    public String delete(@PathVariable Long id,
                         RedirectAttributes ra)
    {
        plantillaRepository.deleteById(id);

        ra.addFlashAttribute("mensaje",
                "Bitácora eliminada con éxito");

        return "redirect:/view/bitacoras";
    }
}