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

@Controller
public class EstadosView {

    @Autowired
    private EstadosRepository estadosRepository;

    /*
     * =========================================================
     * LISTAR ESTADOS
     * =========================================================
     */

    @GetMapping("/view/estados")
    public String lista(Model model) {

        model.addAttribute(
                "estados",
                estadosRepository.findAll()
        );

        return "estados/estados";
    }

    /*
     * =========================================================
     * FORMULARIO NUEVO ESTADO
     * =========================================================
     */

    @GetMapping("/view/estados/form")
    public String form(Model model) {

        model.addAttribute(
                "estados",
                new Estados()
        );

        return "estados/estadosForm";
    }

    /*
     * =========================================================
     * GUARDAR ESTADO
     * =========================================================
     */

    @PostMapping("/view/estados/save")
    public String save(
            @ModelAttribute Estados estados,
            RedirectAttributes ra) {

        estadosRepository.save(estados);

        ra.addFlashAttribute(
                "mensaje",
                "Estado registrado con éxito"
        );

        return "redirect:/view/estados";
    }

    /*
     * =========================================================
     * EDITAR ESTADO
     * =========================================================
     */

    @GetMapping("/view/estados/edit/{id}")
    public String edit(
            @PathVariable Integer id,
            Model model,
            RedirectAttributes ra) {

        Estados estados = estadosRepository
                .findById(id)
                .orElse(null);

        if (estados == null) {

            ra.addFlashAttribute(
                    "mensaje",
                    "El estado solicitado no existe"
            );

            return "redirect:/view/estados";
        }

        model.addAttribute(
                "estados",
                estados
        );

        return "estados/estadosForm";
    }

    /*
     * =========================================================
     * ELIMINAR ESTADO
     * =========================================================
     */

    @PostMapping("/view/estados/delete/{id}")
    public String delete(
            @PathVariable Integer id,
            RedirectAttributes ra) {

        if (!estadosRepository.existsById(id)) {

            ra.addFlashAttribute(
                    "mensaje",
                    "El estado solicitado no existe"
            );

            return "redirect:/view/estados";
        }

        estadosRepository.deleteById(id);

        ra.addFlashAttribute(
                "mensaje",
                "Estado eliminado con éxito"
        );

        return "redirect:/view/estados";
    }
}