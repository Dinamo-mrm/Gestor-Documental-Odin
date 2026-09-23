package com.odin.odin.view;

import com.odin.odin.model.Documentos;
import com.odin.odin.repository.DocumentosRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/view/documentos")
public class DocumentosView {
    private final DocumentosRepository documentosRepository;

    public DocumentosView(DocumentosRepository documentosRepository) {
        this.documentosRepository = documentosRepository;
    }

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("documentos", documentosRepository.findAll());
        return "documentos/documentos";
    }

    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("documentos", new Documentos());
        return "documentos/documentosForm";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Documentos documentos = documentosRepository.findById(id).orElse(null);
        if (documentos == null) {
            ra.addFlashAttribute("error", "El documento no existe.");
            return "redirect:/view/documentos";
        }
        model.addAttribute("documentos", documentos);
        return "documentos/documentosForm";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("documentos") Documentos documentos, RedirectAttributes ra) {
        documentosRepository.save(documentos);
        ra.addFlashAttribute("mensaje", "Documento registrado exitosamente.");
        return "redirect:/view/documentos";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        if (documentosRepository.existsById(id)) {
            documentosRepository.deleteById(id);
            ra.addFlashAttribute("mensaje", "Documento eliminado exitosamente.");
        } else {
            ra.addFlashAttribute("error", "El documento no existe.");
        }
        return "redirect:/view/documentos";
    }
}
