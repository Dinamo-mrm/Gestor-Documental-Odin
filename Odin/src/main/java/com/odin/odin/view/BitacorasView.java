package com.odin.odin.view;

import com.odin.odin.model.Bitacoras;
import com.odin.odin.repository.BitacorasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BitacorasView
{
    @Autowired
    private BitacorasRepository bitacorasRepository;

    // LISTA
    @GetMapping("/view/bitacoras")
    public String lista(Model model)
    {
        model.addAttribute("bitacoras", bitacorasRepository.findAll());

        return "bitacoras/bitacoras";
    }

    // FORMULARIO
    @GetMapping("/view/bitacoras/form")
    public String form(Model model)
    {
        model.addAttribute("bitacora", new Bitacoras());

        return "bitacoras/bitacorasForm";
    }

    // GUARDAR
    @PostMapping("/view/bitacoras/save")
    public String save(@ModelAttribute Bitacoras bitacora,
                       RedirectAttributes ra)
    {
        bitacorasRepository.save(bitacora);

        ra.addFlashAttribute("mensaje",
                "Bitácora registrada con éxito");

        return "redirect:/view/bitacoras";
    }

    // EDITAR
    @GetMapping("/view/bitacoras/edit/{id}")
    public String edit(@PathVariable Long id,
                       Model model)
    {
        Bitacoras bitacora =
                bitacorasRepository.findById(id).orElse(null);

        model.addAttribute("bitacora", bitacora);

        return "bitacoras/bitacorasForm";
    }

    // ELIMINAR
    @PostMapping("/view/bitacoras/delete/{id}")
    public String delete(@PathVariable Long id,
                         RedirectAttributes ra)
    {
        bitacorasRepository.deleteById(id);

        ra.addFlashAttribute("mensaje",
                "Bitácora eliminada con éxito");

        return "redirect:/view/bitacoras";
    }
}