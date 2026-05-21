package com.odin.odin.view;

import com.odin.odin.model.Series;
import com.odin.odin.model.Usuarios;
import com.odin.odin.repository.SeriesRepository;
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
public class SeriesView
{
    @Autowired
    private SeriesRepository repository;

    @GetMapping("/view/series")
    public String lista(Model model)
    {
        model.addAttribute("series", repository.findAll());
        return "series/series";
    }

    @GetMapping("/view/series/form")
    public String form(Model model)
    {
        model.addAttribute("series", new Series());
        return "series/seriesForm";
    }

    @PostMapping("/view/series/save")
    public String save(@ModelAttribute Series series, RedirectAttributes ra)
    {
        repository.save(series);
        ra.addFlashAttribute("mensaje", "Usuario Registrado con Exito");
        return "redirect:/view/series";
    }

    @GetMapping("/view/series/edit/{id}")
    public String edit(@PathVariable Long id, Model model)
    {
        Series series = repository.findById(id).orElse(null);
        model.addAttribute("series", series);
        return "series/seriesForm";
    }

    @PostMapping("/view/series/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra)
    {
        repository.deleteById(id);
        ra.addFlashAttribute("mensaje", "Serie Eliminada con Exito");
        return "redirect:/view/series";
    }


}
