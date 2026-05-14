package com.odin.odin.view;

import com.odin.odin.model.Series;
import com.odin.odin.model.Subseries;
import com.odin.odin.repository.SeriesRepository;
import com.odin.odin.repository.SubseriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SubseriesView
{
    @Autowired
    private SubseriesRepository repository;

    @GetMapping("/view/subseries")
    public String lista(Model model)
    {
        model.addAttribute("subseries", repository.findAll());
        return "subseries/subseries";
    }

    @GetMapping("/view/subseries/form")
    public String form(Model model)
    {
        model.addAttribute("subseries", new Subseries());
        return "subseries/subseriesForm";
    }

    @PostMapping("/view/subseries/save")
    public String save(@ModelAttribute Subseries subseries, RedirectAttributes ra)
    {
        repository.save(subseries);
        ra.addFlashAttribute("mensaje", "Subserie Registrada con Exito");
        return "redirect:/view/subseries";
    }

    @GetMapping("/view/subseries/edit/{id}")
    public String edit(@PathVariable Long id, Model model)
    {
        Subseries subseries = repository.findById(id).orElse(null);
        model.addAttribute("subseries", subseries);
        return "subseries/subseriesForm";
    }

    @PostMapping("/view/subseries/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra)
    {
        repository.deleteById(id);
        ra.addFlashAttribute("mensaje", "Subserie Eliminada con Exito");
        return "redirect:/view/subseries";
    }


}
