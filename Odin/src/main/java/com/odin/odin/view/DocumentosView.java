package com.odin.odin.view;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

import com.odin.odin.model.Documentos;
import com.odin.odin.model.Usuarios;
import com.odin.odin.repository.DocumentosRepository;
import com.odin.odin.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Vista Thymeleaf de Documentos.
 */
@Controller
public class DocumentosView {
    private static final int PAGE_SIZE = 10;

    @Autowired
    private DocumentosRepository documentosRepository;

    @Autowired(required = false)
    private UsuariosRepository usuariosRepository;

    @GetMapping("/view/documentos")
    public String lista(@RequestParam(defaultValue = "0") int page, Model model) {
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<?> pagina = documentosRepository.findAll(pageable);
        model.addAttribute("documentos", pagina.getContent());
        model.addAttribute("page", pagina);
        model.addAttribute("currentPage", pagina.getNumber());
        model.addAttribute("totalPages", Math.max(pagina.getTotalPages(), 1));
        return "documentos/documentos";
    }

    @GetMapping("/view/documentos/form")
    public String form(Model model) {
        model.addAttribute("documentos", new Documentos());
        Usuarios usuario = new Usuarios();
        usuario.setEstado("Activo");
        model.addAttribute("usuarios", usuario);
        return "documentos/documentosForm";
    }

    @PostMapping("/view/documentos/save")
    public String save(@ModelAttribute("usuarios") Usuarios usuario, RedirectAttributes ra) {
        if (usuariosRepository != null && usuario != null) {
            usuariosRepository.save(usuario);
        }
        ra.addFlashAttribute("mensaje", "Documento guardado exitosamente");
        return "redirect:/view/documentos";
    }

    @GetMapping("/view/documentos/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Documentos documento = documentosRepository.findById(id).orElse(new Documentos());
        model.addAttribute("documentos", documento);

        Usuarios usuario = null;
        if (usuariosRepository != null) {
            usuario = usuariosRepository.findById(id).orElse(null);
        }
        if (usuario == null) {
            usuario = new Usuarios();
            usuario.setEstado("Activo");
            if (documento != null && documento.getNombre() != null) {
                usuario.setNombre(documento.getNombre());
            }
        }
        model.addAttribute("usuarios", usuario);
        return "documentos/documentosForm";
    }

    @PostMapping("/view/documentos/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        if (documentosRepository.existsById(id)) {
            documentosRepository.deleteById(id);
            ra.addFlashAttribute("mensaje", "Documento eliminado exitosamente");
        } else {
            ra.addFlashAttribute("mensaje", "Documento no encontrado");
        }
        return "redirect:/view/documentos";
    }
}
