package com.odin.odin.view;

import com.odin.odin.model.Roles;
import com.odin.odin.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class Rolesview
{
    @Autowired
    private RolesRepository rolesRepository;

    @GetMapping("/view/roles")
    public String lista(Model model)
    {
        model.addAttribute("roles", rolesRepository.findAll());
        return "roles/roles";
    }

    @GetMapping("/view/roles/form")
    public String form(Model model)
    {
        model.addAttribute("roles", new Roles());
        return "roles/rolesForm";
    }

    @PostMapping("/view/roles/save")
    public String save(@ModelAttribute Roles roles, RedirectAttributes ra)
    {
        rolesRepository.save(roles);
        ra.addFlashAttribute("mensaje", "rol registrado exitosamente");
        return "redirect:/view/roles";
    }

    @GetMapping("/view/roles/edit/{id}")
    public String edit(@PathVariable long id, Model model)
    {
        Roles roles = rolesRepository.findById(id).orElse(null);
        model.addAttribute("roles", roles);
        return "roles/rolesForm";
    }

    @PostMapping("/view/roles/delete/{id}")
    public String delete(@PathVariable long id, RedirectAttributes ra)
    {
        rolesRepository.deleteById(id);
        ra.addFlashAttribute("mensaje", "rol eliminado exitosamente");
        return "redirect:/view/roles";
    }


}