package com.odin.odin.view;

import com.odin.odin.model.Estados;
import com.odin.odin.repository.EstadosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EstadosView {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private EstadosRepository estadosRepository;

    @GetMapping("/view/estados")
    public String lista(@RequestParam(defaultValue = "0") int page, Model model) {
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<Estados> pagina = estadosRepository.findAll(pageable);
        model.addAttribute("estados", pagina.getContent());
        model.addAttribute("page", pagina);
        model.addAttribute("currentPage", pagina.getNumber());
        model.addAttribute("totalPages", Math.max(pagina.getTotalPages(), 1));
        return "estados/estados";
    }

    @GetMapping("/view/estados/form")
    public String form(Model model) {
        model.addAttribute("estados", new Estados());
        return "estados/estadosForm";
    }

    @PostMapping("/view/estados/save")
    public String save(@ModelAttribute Estados estados, RedirectAttributes ra) {
        estadosRepository.save(estados);
        ra.addFlashAttribute("mensaje", "Estado registrado con éxito");
        return "redirect:/view/estados";
    }

    @GetMapping("/view/estados/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Estados estados = estadosRepository.findById(id).orElse(null);
        model.addAttribute("estados", estados != null ? estados : new Estados());
        return "estados/estadosForm";
    }

    @PostMapping("/view/estados/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        estadosRepository.deleteById(id);
        ra.addFlashAttribute("mensaje", "Estado eliminado con éxito");
        return "redirect:/view/estados";
    }
}
