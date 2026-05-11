package com.odin.odin.view;

import com.odin.odin.model.Tramites;
import com.odin.odin.model.Usuarios;
import com.odin.odin.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TramitesView
{
    @Autowired
    private UsuariosRepository TramitesRepository;

    @GetMapping("/view/tramites")
    public String lista(Model model)
    {
        model.addAttribute("tramites", TramitesRepository.findAll());
        return "tramites/tramites";
    }

    @GetMapping("/view/tramites/form")
    public String form(Model model)
    {
        model.addAttribute("tramites", new Tramites());
        return "tramites/tramitesForm";
    }

    @PostMapping("/view/tramites/save")
    public String save(@ModelAttribute Usuarios usuarios, RedirectAttributes ra)
    {
        TramitesRepository.save(usuarios);
        ra.addFlashAttribute("mensaje", "tramite Registrado con Exito");
        return "redirect:/view/tramites";
    }

    @GetMapping("/view/tramites/edit/{id}")
    public String edit(@PathVariable Long id, Model model)
    {
        Usuarios usuarios = TramitesRepository.findById(id).orElse(null);
        model.addAttribute("tramites", tramites);
        return "tramites/TramitesForm";
    }

    @PostMapping("/view/tramites/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra)
    {
        TramitesRepository.deleteById(id);
        ra.addFlashAttribute("mensaje", "Usuario Eliminado con Exito");
        return "redirect:/view/TramitesView";
    }


}
