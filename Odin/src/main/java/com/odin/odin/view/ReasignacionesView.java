package com.odin.odin.view;

import com.odin.odin.model.Reasignaciones;
import com.odin.odin.repository.ReasignacionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class ReasignacionesView {


    @Autowired
    private ReasignacionesRepository reasignacionesRepository;

    // Llena la tabla que muestra la info de las reasignaciones
    @GetMapping("/view/reasignaciones")
    public String lista(Model model) {
        model.addAttribute("reasignaciones", reasignacionesRepository.findAll());
        return "reasignaciones";
    }

    // Muestra el formulario vacío para crear
    @GetMapping("/view/reasignaciones/form")
    public String form(Model model) {
        model.addAttribute("reasignaciones", new Reasignaciones());
        return "reasignacionesForm"; // Apunta al archivo reasignacionesForm.html
    }

    // Sirve para guardar la información del formulario
    @PostMapping("/view/reasignaciones/save")
    public String save(@ModelAttribute Reasignaciones reasignaciones, RedirectAttributes ra) {
        reasignacionesRepository.save(reasignaciones);
        ra.addFlashAttribute("mensaje", "Reasignación registrada con éxito");
        return "redirect:/view/reasignaciones";
    }

    // Carga los datos en el formulario para editar
    @GetMapping("/view/reasignaciones/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Reasignaciones reasignaciones = reasignacionesRepository.findById(id).orElse(null);
        model.addAttribute("reasignaciones", reasignaciones);
        return "reasignacionesForm";
    }

    // Borrar reasignaciones
    @PostMapping("/view/reasignaciones/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        reasignacionesRepository.deleteById(id);
        ra.addFlashAttribute("mensaje", "Reasignación eliminada con éxito");
        return "redirect:/view/reasignaciones";
    }
}