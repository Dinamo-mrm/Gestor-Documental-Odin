package com.odin.odin.view;

import com.odin.odin.model.Estados;
import com.odin.odin.repository.EstadosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//devuelve un archivo tipo HTML
@Controller
public class EstadosView
{
    //comuniaccion con la base de datos a tra vez del jpa
    @Autowired
    private EstadosRepository estadosRepository;

    //llena la tabla que muetra la info de los usuarios
    @GetMapping("/view/estados")
    public String lista(Model model)
    {
        model.addAttribute("estados", estadosRepository.findAll());
        return "estados/estados";
    }

    @GetMapping("/view/estados/form")
    public String form(Model model)
    {
        model.addAttribute("estados", new Estados());
        return "estados/estadosForm";
    }

    //sirve para guardar la lista
    @PostMapping("/view/estados/save")
    public String save(@ModelAttribute Estados estados, RedirectAttributes ra)
    {
        estadosRepository.save(estados);
        ra.addFlashAttribute("mensaje", "Estado registrado con exito");
        return "redirect:/view/estados";
    }

    //editar estados
    @GetMapping("/view/estados/edit/{id}")
    public String edit(@PathVariable Long id, Model model)
    {
        Estados estados = estadosRepository.findById(id).orElse(null);
        model.addAttribute("estados", estados);
        return "estadosForm";
    }

    //borrar estados
    @PostMapping("/view/estados/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra)
    {
        estadosRepository.deleteById(id);
        ra.addFlashAttribute("mensaje", "Estado eliminado con exito");
        return "redirect:/view/estados";
    }
}