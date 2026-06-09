package com.odin.odin.view;

import com.odin.odin.model.Series;
import com.odin.odin.repository.CcdUnidadRepository;
import com.odin.odin.repository.SeriesRepository;
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

    @Autowired
    private CcdUnidadRepository unidadesRepository;

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
        model.addAttribute("unidadesList", unidadesRepository.findAll());
        return "series/seriesForm";
    }

    @PostMapping("/view/series/save")
    public String save(@ModelAttribute Series series, RedirectAttributes ra)
    {
        boolean isUpdate = series.getId_serie() != null;
        Series savedSeries = repository.save(series);
        Series loadedSeries = repository.findById(savedSeries.getId_serie()).orElse(savedSeries);

        ra.addFlashAttribute("success", isUpdate ? "Serie actualizada con exito" : "Serie registrada con exito");
        ra.addFlashAttribute("savedSeries", loadedSeries);
        return "redirect:/view/series";
    }

    @GetMapping("/view/series/edit/{id}")
    public String edit(@PathVariable Long id, Model model)
    {
        Series series = repository.findById(id).orElse(new Series());
        model.addAttribute("series", series);
        model.addAttribute("unidadesList", unidadesRepository.findAll());
        return "series/seriesForm";
    }

    @PostMapping("/view/series/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra)
    {
        repository.deleteById(id);
        ra.addFlashAttribute("success", "Serie eliminada con exito");
        return "redirect:/view/series";
    }


}
