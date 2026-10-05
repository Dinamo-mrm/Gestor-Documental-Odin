package com.odin.odin.view;

import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Page;

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
    private static final int PAGE_SIZE = 10;

    @Autowired
    private SubseriesRepository repository;

    @Autowired
    private SeriesRepository seriesRepository;

    @GetMapping("/view/subseries")
    public String lista(@RequestParam(defaultValue = "0") int page, Model model)
    {
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<?> pagina = repository.findAll(pageable);
        model.addAttribute("subseries", pagina.getContent());
        model.addAttribute("page", pagina);
        model.addAttribute("currentPage", pagina.getNumber());
        model.addAttribute("totalPages", Math.max(pagina.getTotalPages(), 1));
        return "subseries/subseries";
    }

    @GetMapping("/view/subseries/form")
    public String form(Model model)
    {
        model.addAttribute("subseries", new Subseries());
        model.addAttribute("seriesList", seriesRepository.findAll());
        return "subseries/subseriesForm";
    }

    @PostMapping("/view/subseries/save")
    public String save(@ModelAttribute Subseries subseries, RedirectAttributes ra)
    {
        boolean isUpdate = subseries.getId_subserie() != null;
        Subseries savedSubseries = repository.save(subseries);
        Subseries loadedSubseries = repository.findById(savedSubseries.getId_subserie()).orElse(savedSubseries);

        ra.addFlashAttribute("success", isUpdate ? "Subserie actualizada con exito" : "Subserie registrada con exito");
        ra.addFlashAttribute("savedSubseries", loadedSubseries);
        return "redirect:/view/subseries";
    }

    @GetMapping("/view/subseries/edit/{id}")
    public String edit(@PathVariable Long id, Model model)
    {
        Subseries subseries = repository.findById(id).orElse(new Subseries());
        model.addAttribute("subseries", subseries);
        model.addAttribute("seriesList", seriesRepository.findAll());
        return "subseries/subseriesForm";
    }

    @PostMapping("/view/subseries/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra)
    {
        repository.deleteById(id);
        ra.addFlashAttribute("success", "Subserie eliminada con exito");
        return "redirect:/view/subseries";
    }


}
