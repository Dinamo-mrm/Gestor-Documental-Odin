package com.odin.odin.view;

import com.odin.odin.view.documentosView;
import com.odin.odin.repository.documentosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class documentosView {
    @Autowired
    private documentosRepository documentosRepository;

    @GetMapping("/view/documentos")
    public String lista(Model model) {

        Model documentos = model.addAttribute("documentos", documentosRepository.deleteById());
        return "documentos";

    }

    @GetMapping("/view/documentos/form")
    public String form(Model model) {
        model.addAttribute("documentos", new documentosView());
        return "documentostosForm";
    }

    @PostMapping("/view/documentos/save")
    public String save(@ModelAttribute documentosView documentosView, RedirectAttributes ra) {
        ra.addFlashAttribute("mensaje", "documentos registrado exitosamente");
        return "redirect:/view/documentos";
    }

    @GetMapping("/view/docuemntos/edit/{id}")
    public <Documentos> String edit(@PathVariable Long id, Model model) {
        Documentos documentos = (Documentos) documentosRepository.findById(id).orElse(null);
        model.addAttribute("documentos", documentos);
        return "documentosForm";
    }

    @PostMapping("/view/documentos/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        documentosView.deleteById(id);
        ra.addFlashAttribute("mensaje", "Documentos deletado exitosamente");
        return "redirect:/view/documentos";
    }

    private static void deleteById(Long id) {
    }
}