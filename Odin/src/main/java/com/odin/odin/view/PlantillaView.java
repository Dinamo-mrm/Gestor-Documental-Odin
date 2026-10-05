package com.odin.odin.view;

import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Page;

import com.odin.odin.model.Plantilla;
import com.odin.odin.repository.PlantillaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Vista de la tabla plantilla (trazabilidad).
 *
 * IMPORTANTE: NO mapea /view/bitacora porque el proyecto ya tiene BitacoraView
 * para esa ruta. Solo mantiene /view/plantilla y rutas de formulario/CRUD.
 */
@Controller
public class PlantillaView {
    private static final int PAGE_SIZE = 10;


    @Autowired
    private PlantillaRepository plantillaRepository;

    @GetMapping("/view/plantilla")
    public String lista(@RequestParam(defaultValue = "0") int page, Model model) {
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<?> pagina = plantillaRepository.findAll(pageable);
        model.addAttribute("plantilla", pagina.getContent());
        model.addAttribute("page", pagina);
        model.addAttribute("currentPage", pagina.getNumber());
        model.addAttribute("totalPages", Math.max(pagina.getTotalPages(), 1));
        model.addAttribute("registros", pagina.getContent());
        return "Plantilla/Plantilla";
    }

    @GetMapping({"/view/plantilla/form", "/view/bitacoras/form"})
    public String form(Model model) {
        model.addAttribute("plantilla", new Plantilla());
        return "Plantilla/PlantillaForm";
    }

    @PostMapping({"/view/plantilla/save", "/view/bitacoras/save"})
    public String save(@ModelAttribute Plantilla bitacora, RedirectAttributes ra) {
        plantillaRepository.save(bitacora);
        ra.addFlashAttribute("mensaje", "Registro guardado con éxito");
        return "redirect:/view/plantilla";
    }

    @GetMapping({"/view/plantilla/edit/{id}", "/view/bitacoras/edit/{id}"})
    public String edit(@PathVariable Long id, Model model) {
        Plantilla bitacora = plantillaRepository.findById(id).orElse(null);
        model.addAttribute("plantilla", bitacora);
        model.addAttribute("bitacora", bitacora);
        return "Plantilla/PlantillaForm";
    }

    @PostMapping({"/view/plantilla/delete/{id}", "/view/bitacoras/delete/{id}"})
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        plantillaRepository.deleteById(id);
        ra.addFlashAttribute("mensaje", "Registro eliminado con éxito");
        return "redirect:/view/plantilla";
    }
}